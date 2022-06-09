package me.chat.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.chat.common.LogHelper;
import me.chat.connection.impl.ChatConnection;
import me.chat.protocol.AbstractPacket;
import me.chat.protocol.HandshakePacket;

import java.net.InetAddress;
import java.net.InetSocketAddress;

@RequiredArgsConstructor
public class InitialHandler extends SimpleChannelInboundHandler<AbstractPacket> {

    private final ChatConnection chatConnection;

    @SneakyThrows
    public void channelActive(ChannelHandlerContext ctx) {
        LogHelper.info("InitialHandler has connected...");
        HandshakePacket packet = new HandshakePacket();
        packet.setHostname(InetAddress.getLocalHost().getHostAddress());
        ctx.writeAndFlush(packet);
    }

    public void channelInactive(ChannelHandlerContext ctx) {
        LogHelper.info("InitialHandler has disconnected!");
        this.chatConnection.getConnector().reconnect();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LogHelper.info("[/" + InitialHandler.getChannelIp(ctx.channel()) + "] InitialHandler ERROR: " + cause.getMessage());
        ctx.close();
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, AbstractPacket packet) {
        if (packet instanceof HandshakePacket) {
            LogHelper.info("InitialHandler has read Handshake! Changing pipeline...");
            channelHandlerContext.pipeline().removeLast();
            channelHandlerContext.pipeline().addLast(new PacketHandler(this.chatConnection));
            this.chatConnection.setChannel(channelHandlerContext);
            return;
        }

        LogHelper.info("First packet must be Handshake! Disconnecting...");
        channelHandlerContext.close();
    }

    public static String getChannelIp(Channel channel) {
        return ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
    }
}

