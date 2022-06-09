package me.chat.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.RequiredArgsConstructor;
import me.chat.connection.impl.ChatConnection;

@RequiredArgsConstructor
public class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final ChatConnection chatConnection;

    public void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new PacketDecoder());
        pipeline.addLast(new PacketEncoder());
        pipeline.addLast(new InitialHandler(this.chatConnection));
    }
}

