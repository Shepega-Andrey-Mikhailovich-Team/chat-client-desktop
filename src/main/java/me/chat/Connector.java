package me.chat;

import com.sun.javafx.application.PlatformImpl;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import me.chat.common.IOHelper;
import me.chat.common.LogHelper;
import me.chat.connection.impl.ChatConnection;
import me.chat.controller.ChatController;
import me.chat.controller.LoginController;
import me.chat.listener.ListenerManager;
import me.chat.netty.NettyChannelInitializer;
import me.chat.protocol.AbstractPacket;
import me.chat.protocol.LoginPacket;
import me.chat.protocol.UserMessagePacket;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class Connector {

    @Getter
    private final ChatConnection defaultConnection;

    private Bootstrap bootstrap;
    private EventLoopGroup group;
    private Channel channel;
    private ScheduledFuture reconnectScheduledFuture;

    @Setter
    @Getter
    private String userName;

    private final String hostname = "localhost";
    private final int port = 5000;

    @Getter
    private final ListenerManager listenerManager = new ListenerManager();

    @SneakyThrows
    public void start() {
        LogHelper.info("Registering listeners...");

        this.listenerManager.registerListener(UserMessagePacket.class, abstractPacket -> {
            UserMessagePacket packet = (UserMessagePacket) abstractPacket;
            ChatController.getInstance().getTxtArea().appendText(packet.getUserName() + ": " + packet.getMessage() + "\n");
        });

        this.listenerManager.registerListener(LoginPacket.class, abstractPacket -> {
            LoginPacket packet = (LoginPacket) abstractPacket;
            if (!packet.isAllowed())
                return;

            PlatformImpl.runLater(this::startChat);
        });


        LogHelper.info("Creating NioEventLoopGroup...");
        this.group = Epoll.isAvailable() ? new EpollEventLoopGroup(1) : new NioEventLoopGroup(1);
        LogHelper.info("Creating Bootstrap...");
        this.bootstrap = new Bootstrap().group(this.group).channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class).handler(new NettyChannelInitializer(this.defaultConnection));
        LogHelper.info("Connecting to the core...");
        this.group.execute(() -> {
            if (!this.connect())
                this.reconnect();
        });
    }

    @SneakyThrows
    private void startChat() {
        FXMLLoader fxmlLoader = new FXMLLoader(IOHelper.getResourceURL("dialog/chat.fxml"));
        Scene chatScene = new Scene(fxmlLoader.load(), 600, 411, Color.TRANSPARENT);
        chatScene.getStylesheets().add(IOHelper.getResourceURL("dialog/chat.css").toExternalForm());
        Stage primaryStage = (Stage) LoginController.getInstance().getPane().getScene().getWindow();
        if (chatScene != null) {
            primaryStage.setScene(chatScene);
            primaryStage.centerOnScreen();
        }
    }

    protected boolean connect() {
        try {
            ChannelFutureListener listener = channelFuture -> {
                if (channelFuture.isSuccess()) {
                    this.channel = channelFuture.channel();
                    LogHelper.info("Connected to the Core [/" + this.hostname + ":" + this.port + "]");
                } else {
                    LogHelper.info("Could not connect to the Core [/" + this.hostname + ":" + this.port + "]: " + channelFuture.cause().getMessage());
                }
            };

            this.bootstrap.connect(this.hostname, this.port).addListener(listener).syncUninterruptibly();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public void reconnect() {
        if (!this.isConnected() && (this.reconnectScheduledFuture == null || this.reconnectScheduledFuture.isCancelled())) {
            this.reconnectScheduledFuture = this.group.scheduleWithFixedDelay(() -> {
                if (this.isConnected()) {
                    this.reconnectScheduledFuture.cancel(false);
                    return;
                }

                LogHelper.info("Reconnecting to the Core...");
                if (this.connect())
                    this.reconnectScheduledFuture.cancel(false);
            }, 5L, 5L, TimeUnit.SECONDS);
        }
    }

    public void onDisconnect() {
        LogHelper.info("Lost connection to the Core... Waiting to reconnect...");
        this.channel = null;
        this.reconnect();
    }

    public void stop() {
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

