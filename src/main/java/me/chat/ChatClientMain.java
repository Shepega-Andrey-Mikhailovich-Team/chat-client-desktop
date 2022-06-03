package me.chat;

import me.chat.dialog.ErrorDialog;
import me.chat.dialog.LoginDialog;

public class ChatClientMain implements Runnable {
    
    public ChatClientMain() {
        // EVENT_BUS.register(PlayerListener.class);
    }

    public static void main(String[] args) {
        new ChatClientMain().run();
    }

    @Override
    public void run() {
        if (Float.parseFloat(System.getProperty("java.class.version")) < 52.0) {
            ErrorDialog.show("Java Version", new Throwable("Need Java 8+"), "Для запуска потрібна jdk 8+");
            return;
        }

        LoginDialog.launch(LoginDialog.class);
    }
}
