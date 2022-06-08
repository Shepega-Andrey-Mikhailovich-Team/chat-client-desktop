package me.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.chat.common.LogHelper;
import me.chat.controller.ChatController;
import me.chat.listener.ListenerManager;
import me.chat.netty.NettyChannelInitializer;
import me.chat.protocol.AbstractPacket;
import me.chat.protocol.UserMessagePacket;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Connector {

    @Getter
    private static boolean debug;

    @Getter
    private static Connector instance;

    private Bootstrap bootstrap;
    private EventLoopGroup group;
    private Channel channel;
    private ScheduledFuture reconnectScheduledFuture;


    @Setter
    @Getter
    private String name, host;
    private int port;

    private boolean enabled;

    @Getter
    private final ListenerManager listenerManager = new ListenerManager();

    public static void setInstance(Connector instance) {
        if (instance == null)
            throw new RuntimeException("Instance cannot be null!");

        if (Connector.instance != null)
            throw new RuntimeException("Instance already set!");

        Connector.instance = instance;
    }

    public Connector(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @SneakyThrows
    public void start() {
        this.enabled = true;
        LogHelper.info("Registering listeners...");

        this.listenerManager.registerListener(UserMessagePacket.class, abstractPacket -> {
            UserMessagePacket packet = (UserMessagePacket) abstractPacket;
            LogHelper.info(packet.getMessage());
            ChatController.instance.getTxtArea().appendText(packet.getUserName() + ": " + packet.getMessage() + "\n");
        });

        LogHelper.info("Creating NioEventLoopGroup...");
        this.group = Epoll.isAvailable() ? new EpollEventLoopGroup(1) : new NioEventLoopGroup(1);
        LogHelper.info("Creating Bootstrap...");
        this.bootstrap = new Bootstrap().group(this.group).channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class).handler(new NettyChannelInitializer());
        LogHelper.info("Connecting to the core...");
        this.group.execute(() -> {
            if (!this.connect())
                this.reconnect();
        });
    }

    protected boolean connect() {
        try {
            ChannelFutureListener listener = channelFuture -> {
                if (channelFuture.isSuccess()) {
                    this.channel = channelFuture.channel();
                    LogHelper.info("Connected to the Core [/" + this.host + ":" + this.port + "]");
                } else {
                    LogHelper.info("Could not connect to the Core [/" + this.host + ":" + this.port + "]: " + channelFuture.cause().getMessage());
                }
            };
            this.bootstrap.connect(this.host, this.port).addListener(listener).syncUninterruptibly();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public void reconnect() {
        if (this.enabled && !this.isConnected() && (this.reconnectScheduledFuture == null || this.reconnectScheduledFuture.isCancelled())) {
            this.reconnectScheduledFuture = this.group.scheduleWithFixedDelay(() -> {
                if (this.isConnected()) {
                    this.reconnectScheduledFuture.cancel(false);
                    return;
                }

                LogHelper.info("Reconnecting to the Core...");
                if (this.connect()) {
                    this.reconnectScheduledFuture.cancel(false);
                }
            }, 5L, 5L, TimeUnit.SECONDS);
        }
    }

    public void onDisconnect() {
        LogHelper.info("Lost connection to the Core... Waiting to reconnect...");
        this.channel = null;
        this.reconnect();
    }

    public void stop() {
        this.enabled = false;
        if (this.reconnectScheduledFuture != null)
            this.reconnectScheduledFuture.cancel(false);

        LogHelper.info("Closing netty channel...");
        this.channel.close().syncUninterruptibly();
        LogHelper.info("Shutdowning NioEventLoopGroup...");
        this.group.shutdownGracefully();
        LogHelper.info("Closed!");
        System.exit(0);
    }

    public boolean isConnected() {
        return this.channel != null;
    }

    public void sendPacket(AbstractPacket packet) {
        if (this.channel != null && this.channel.isActive())
            this.channel.writeAndFlush(packet);
    }
}

