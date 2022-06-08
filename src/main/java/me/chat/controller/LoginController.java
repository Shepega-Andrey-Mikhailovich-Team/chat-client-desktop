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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import me.chat.ChatClientMain;
import me.chat.Connector;
import me.chat.common.IOHelper;
import me.chat.common.VerifyHelper;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private Scene chatScene;

    @FXML
    private TextField textbox;

    @FXML
    private Button button;

    @FXML
    private Pane pane;

    @FXML
    private TextArea txtArea;

    @FXML
    protected void enterPressedEvent(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER)
            this.enterUser();
    }

    @FXML
    protected void buttonClickEvent() {
        this.enterUser();
    }

    @FXML
    protected void hyperlink() throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://github.com/Shepega-Andrey-Mikhailovich-Team"));
    }

    private void enterUser() {
        if (!this.fireEvent()) return;
        Stage primaryStage = (Stage) pane.getScene().getWindow();
        if (this.chatScene != null) {
            primaryStage.setScene(this.chatScene);
            primaryStage.centerOnScreen();
        }
    }

    private boolean fireEvent() {
        if (!this.textbox.getText().isEmpty() && VerifyHelper.isValidUsername(this.textbox.getText())) {
            ChatClientMain.setUserName(this.textbox.getText());
            Connector connector = Connector.getInstance();

            if (connector == null) return false;
            connector.start();
            connector.setName(this.textbox.getText());
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

        FXMLLoader fxmlLoader = new FXMLLoader(IOHelper.getResourceURL("dialog/chat.fxml"));
        this.chatScene = new Scene(fxmlLoader.load(), 600, 411, Color.TRANSPARENT);
        this.chatScene.getStylesheets().add(IOHelper.getResourceURL("dialog/chat.css").toExternalForm());
    }

    public void handleNewWindow(ActionEvent actionEvent) {
    }
}