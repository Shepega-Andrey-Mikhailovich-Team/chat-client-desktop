package me.chat;

import me.chat.dialog.ErrorDialog;
import me.chat.dialog.LoginDialog;
import me.chat.event.PlayerJoinEvent;
import me.chat.listener.PlayerListener;
import me.chat.ssbus.Bus;

public class ChatClientMain {

    public static final Bus<PlayerJoinEvent> EVENT_BUS = new Bus<>(PlayerJoinEvent.class);

    public static void main(String[] args) {
        if (Float.parseFloat(System.getProperty("java.class.version")) < 52.0) {
            ErrorDialog.show("Java Version", new Throwable("Need Java 8+"), "Для запуска потрібна jdk 8+");
            return;
        }

        EVENT_BUS.register(PlayerListener.class);
        LoginDialog.launch(LoginDialog.class, args);
    }
}
