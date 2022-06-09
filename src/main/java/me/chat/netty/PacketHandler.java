package me.chat.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import me.chat.common.LogHelper;
import me.chat.connection.impl.ChatConnection;
import me.chat.protocol.AbstractPacket;

@RequiredArgsConstructor
public class PacketHandler extends SimpleChannelInboundHandler<AbstractPacket> {

    private final ChatConnection chatConnection;

    public void channelInactive(ChannelHandlerContext ctx) {
        this.chatConnection.getConnector().onDisconnect();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        LogHelper.info("PacketHandler ERROR: " + cause.getMessage());
        ctx.close();
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, AbstractPacket packet) {
        this.chatConnection.getConnector().getListenerManager().handleListeners(packet);
    }
}

