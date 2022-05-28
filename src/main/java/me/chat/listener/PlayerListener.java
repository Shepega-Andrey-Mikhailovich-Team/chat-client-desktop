package me.chat.listener;

import me.chat.dialog.ChatDialog;
import me.chat.event.PlayerJoinEvent;
import me.chat.ssbus.Listener;

public class PlayerListener {

    @Listener
    public static void onEvent(PlayerJoinEvent event) {
        ChatDialog.launch(ChatDialog.class);
    }
}
