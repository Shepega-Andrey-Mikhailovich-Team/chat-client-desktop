package me.chat.connection.impl;

import io.netty.channel.ChannelHandlerContext;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import me.chat.Connector;
import me.chat.connection.DefaultConnection;
import me.chat.protocol.AbstractPacket;
import me.chat.protocol.LoginPacket;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatConnection implements DefaultConnection {

    @Setter
    @NonFinal
    String userName;
    Connector connector;

    @Setter
    @NonFinal
    ChannelHandlerContext channel;

    public ChatConnection() {
        this.connector = new Connector(this);
    }

    @Override
    public void login() {
        this.connector.start();
    }

    @SneakyThrows
    @Override
    public void join() {
        LoginPacket loginPacket = new LoginPacket();
        loginPacket.setUserName(this.userName);
        this.sendPacket(loginPacket);
    }

    @Override
    public void leave(boolean login) {
        if (this.connector != null)
            this.connector.stop();
        // Leave packet
    }

    public void sendPacket(AbstractPacket abstractPacket) {
        if (this.channel != null && this.channel.channel().isActive())
            channel.writeAndFlush(abstractPacket);
    }
}
