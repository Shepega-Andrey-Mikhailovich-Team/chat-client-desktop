package me.chat.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.chat.Connector;
import me.chat.common.LogHelper;
import me.chat.protocol.AbstractPacket;

public class PacketHandler extends SimpleChannelInboundHandler<AbstractPacket> {

    private final Connector connector = Connector.getInstance();

    public void channelInactive(ChannelHandlerContext ctx) {
        this.connector.onDisconnect();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        LogHelper.info("PacketHandler ERROR: " + cause.getMessage());
        ctx.close();
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, AbstractPacket packet) {
        if (Connector.isDebug())
            LogHelper.info("PacketHandler has read: " + packet.toString());

        this.connector.getListenerManager().handleListeners(packet);
    }
}

