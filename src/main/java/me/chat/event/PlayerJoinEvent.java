package me.chat.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PlayerJoinEvent {

    private final String userName;
}
