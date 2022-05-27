package me.chat.listener;

import me.chat.event.PlayerJoinEvent;
import me.chat.ssbus.Listener;

public class PlayerListener {

    @Listener
    public static void onEvent(PlayerJoinEvent event) {
        System.out.println(event.getUserName());
    }
}
