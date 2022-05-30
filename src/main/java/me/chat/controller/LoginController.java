package me.chat.controller;

import com.jfoenix.controls.JFXRippler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.SneakyThrows;
import me.chat.ChatClientMain;
import me.chat.common.IOHelper;
import me.chat.common.VerifyHelper;
import me.chat.event.PlayerJoinEvent;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private Stage chatStage;

    @FXML
    private TextField textbox;

    @FXML
    private Button button;

    @FXML
    private Pane pane;

    @FXML
    protected void enterPressedEvent(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER)
            this.fireEvent();
    }

    @FXML
    protected void buttonClickEvent() throws IOException {
        if (!this.fireEvent()) return;
        Stage primaryStage = (Stage) pane.getScene().getWindow();
        primaryStage.setScene(this.chatStage.getScene());
    }

    @FXML
    protected void hyperlink() throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://github.com/Shepega-Andrey-Mikhailovich-Team"));
    }

    private boolean fireEvent() {
        if (!this.textbox.getText().isEmpty() && VerifyHelper.isValidIDName(this.textbox.getText())) {
            ChatClientMain.EVENT_BUS.unsafeFireAndForget(new PlayerJoinEvent(this.textbox.getText()));
            return true;
        }

        // shake textbox and label

        return false;
    }

    @FXML
    protected void onExit() {
        Platform.exit();
    }

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Добавляємо Ripple ефект на кнопку
        JFXRippler jfxRippler = new JFXRippler(this.button);
        jfxRippler.setRipplerFill(Paint.valueOf("#FDEBAE"));
        jfxRippler.setLayoutX(99);
        jfxRippler.setLayoutY(228);
        pane.getChildren().add(jfxRippler);

        this.chatStage = new Stage();
        this.chatStage.initStyle(StageStyle.UNDECORATED);
        this.chatStage.initStyle(StageStyle.TRANSPARENT);

        FXMLLoader fxmlLoader = new FXMLLoader(IOHelper.getResourceURL("dialog/chat.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 604, 440, Color.TRANSPARENT);
        scene.getStylesheets().add(IOHelper.getResourceURL("dialog/chat.css").toExternalForm());

        // Move handler
        scene.setOnMousePressed(pressEvent -> scene.setOnMouseDragged(dragEvent -> {
            this.chatStage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
            this.chatStage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
        }));

        this.chatStage.getIcons().add(new Image(IOHelper.getResourceURL("assets/img/logo.png").toString()));
        this.chatStage.setTitle("Chat");
        this.chatStage.setScene(scene);
    }

    public void handleNewWindow(ActionEvent actionEvent) {
    }
}