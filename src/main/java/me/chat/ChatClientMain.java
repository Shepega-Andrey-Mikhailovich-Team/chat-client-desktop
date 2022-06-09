package me.chat;

import lombok.Getter;
import me.chat.common.LogHelper;
import me.chat.connection.impl.ChatConnection;
import me.chat.dialog.ErrorDialog;
import me.chat.dialog.LoginDialog;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ChatClientMain implements Runnable {

    @Getter
    private static ChatClientMain instance;

    @Getter
    private static ChatConnection chatConnection;

    public static final Path WORKING_DIR = Paths.get(System.getProperty("user.dir"));

    public ChatClientMain() {
        instance = this;
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

        LogHelper.info("Welcome! Chat Test coded by Shepeha-Team");
        chatConnection = new ChatConnection();
        chatConnection.login();

        LoginDialog.launch(LoginDialog.class);
    }
}
