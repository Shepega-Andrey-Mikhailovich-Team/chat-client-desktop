package me.chat.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    @FXML
    private TextArea txtArea;

    @FXML
    protected void onButtonClick() {
        Platform.exit();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont("assets/fonts/JetBrains Mono Extra Bold Nerd Font Complete.ttf", 10);
    }

    @FXML
    protected void onExit() {
        Platform.exit();
    }
}
