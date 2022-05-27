package me.chat;

import me.chat.dialog.LoginDialog;
import me.chat.event.PlayerJoinEvent;
import me.chat.listener.PlayerListener;
import me.chat.ssbus.Bus;

public class ChatClientMain {

    public static final Bus<PlayerJoinEvent> EVENT_BUS = new Bus<>(PlayerJoinEvent.class);

    public static void main(String[] args) {
        EVENT_BUS.register(PlayerListener.class);
        LoginDialog.launch(LoginDialog.class, args);
    }
}
