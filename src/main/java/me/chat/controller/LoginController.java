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
import me.chat.ChatClientMain;
import me.chat.common.IOHelper;
import me.chat.event.PlayerJoinEvent;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

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
        this.fireEvent();
        Stage stage1 = (Stage) pane.getScene().getWindow();
        stage1.hide();
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initStyle(StageStyle.TRANSPARENT);

        FXMLLoader fxmlLoader = new FXMLLoader(IOHelper.getResourceURL("dialog/chat.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 604, 440, Color.TRANSPARENT);
        scene.getStylesheets().add(IOHelper.getResourceURL("dialog/chat.css").toExternalForm());

        // Move handler
        scene.setOnMousePressed(pressEvent -> scene.setOnMouseDragged(dragEvent -> {
            stage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
            stage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
        }));

        stage.getIcons().add(new Image(IOHelper.getResourceURL("assets/img/logo.png").toString()));
        stage.setTitle("Chat");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void hyperlink() throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://github.com/Shepega-Andrey-Mikhailovich-Team"));
    }

    private void fireEvent() {
        if (!this.textbox.getText().isEmpty()) {
            ChatClientMain.EVENT_BUS.unsafeFireAndForget(new PlayerJoinEvent(this.textbox.getText()));
        }
    }

    @FXML
    protected void onExit() {
        Platform.exit();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Добавляємо Ripple ефект на кнопку
        JFXRippler jfxRippler = new JFXRippler(this.button);
        jfxRippler.setRipplerFill(Paint.valueOf("#FDEBAE"));
        jfxRippler.setLayoutX(99);
        jfxRippler.setLayoutY(228);
        pane.getChildren().add(jfxRippler);
    }

    public void handleNewWindow(ActionEvent actionEvent) {
    }
}