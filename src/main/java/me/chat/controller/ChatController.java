package me.chat.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    @FXML
    private TextArea txtArea;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    protected void onExit() {
        Platform.exit();
    }

    @FXML
    protected void send() {

    }
}
