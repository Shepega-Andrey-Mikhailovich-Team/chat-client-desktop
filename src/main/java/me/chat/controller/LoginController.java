package me.chat.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import me.chat.ChatClientMain;
import me.chat.event.PlayerJoinEvent;

public class LoginController {

    @FXML
    private TextField textbox;

    @FXML
    protected void enterPressedEvent(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER)
            this.fireEvent();
    }

    @FXML
    protected void buttonClickEvent() {
        this.fireEvent();
    }

    private void fireEvent() {
        if (!this.textbox.getText().isEmpty())
            ChatClientMain.EVENT_BUS.unsafeFireAndForget(new PlayerJoinEvent(this.textbox.getText()));
    }

    @FXML
    protected void onExit() {
        Platform.exit();
    }
}