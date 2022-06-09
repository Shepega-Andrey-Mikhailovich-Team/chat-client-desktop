package me.chat.connection.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import me.chat.Connector;
import me.chat.connection.DefaultConnection;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatConnection implements DefaultConnection {

    @Setter
    @NonFinal
    String userName;
    Connector connector;

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
        
    }

    @Override
    public void leave() {
        // Leave packet
    }
}
