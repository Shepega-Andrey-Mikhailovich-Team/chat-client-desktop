package me.chat.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Getter;
import me.chat.ChatClientMain;
import me.chat.Connector;
import me.chat.protocol.UserMessagePacket;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    public static ChatController instance;

    public static void setInstance(ChatController instance) {
        if (instance == null)
            throw new RuntimeException("Instance cannot be null!");

        if (ChatController.instance != null)
            throw new RuntimeException("Instance already set!");

        ChatController.instance = instance;
    }

    @FXML
    Pane pane;

    @Getter
    @FXML
    private TextArea txtArea;

    @FXML
    private Pane movehandler;

    @FXML
    private TextField enterText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.movehandler.setOnMousePressed(pressEvent -> movehandler.setOnMouseDragged(dragEvent -> {
            Stage primaryStage = (Stage) movehandler.getScene().getWindow();
            primaryStage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
            primaryStage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
        }));

        ChatController.setInstance(this);
    }

    @FXML
    protected void onExit() {
        Connector.getInstance().stop();
        Platform.exit();
    }

    @FXML
    protected void onMinimize() {
        // Minimize Window
        ((Stage) pane.getScene().getWindow()).setIconified(true);
    }

    @FXML
    protected void send() {
        if (!this.enterText.getText().isEmpty()) {
            // this.txtArea.appendText();

            UserMessagePacket packet = new UserMessagePacket();
            packet.setUserName(ChatClientMain.getUserName());
            packet.setMessage(this.enterText.getText() + "\n");
            this.enterText.clear();
            Connector.getInstance().sendPacket(packet);
        }
    }

    @FXML
    protected void enter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER)
            this.send();
    }
}

