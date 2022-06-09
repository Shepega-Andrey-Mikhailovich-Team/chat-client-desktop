package me.chat.connection.impl;

import io.netty.channel.ChannelHandlerContext;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import me.chat.Connector;
import me.chat.common.LogHelper;
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
    public void leave() {
        // Leave packet
    }

    public void sendPacket(AbstractPacket abstractPacket) {
        LogHelper.info(String.valueOf(channel.channel().isActive()));
        channel.channel().writeAndFlush(abstractPacket);
    }
}
