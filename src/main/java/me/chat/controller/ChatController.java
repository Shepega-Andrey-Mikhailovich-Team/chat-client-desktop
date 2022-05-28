package me.chat.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;

public class ChatController {
    @FXML
    protected void onButtonClick() {
        Platform.exit();
    }
}
