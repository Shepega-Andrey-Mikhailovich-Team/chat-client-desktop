package me.chat.listener;

import me.chat.protocol.AbstractPacket;

import java.io.IOException;

public interface Listener {
    public void handle(AbstractPacket abstractPacket) throws IOException;
}

