package me.chat.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import me.chat.protocol.AbstractPacket;

import java.util.List;

public class PacketDecoder extends ReplayingDecoder<AbstractPacket> {

    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        out.add(AbstractPacket.readPacket(buf));
    }
}

