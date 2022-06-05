package me.chat.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    @FXML
    Pane pane;

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
    protected void onMinimize() {
        // Minimize Window
        ((Stage) pane.getScene().getWindow()).setIconified(true);
    }

    @FXML
    protected void send() {

    }
}
