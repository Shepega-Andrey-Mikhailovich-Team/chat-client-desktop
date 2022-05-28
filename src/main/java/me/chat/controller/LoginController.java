package me.chat.controller;

import com.jfoenix.controls.JFXRippler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import me.chat.ChatClientMain;
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
    private Hyperlink hyperlink;

    @FXML
    private WebView webView;

    @FXML
    protected void enterPressedEvent(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER)
            this.fireEvent();
    }

    @FXML
    protected void buttonClickEvent() throws IOException {
        this.fireEvent();
        Parent root = FXMLLoader.load(getClass().getResource("assets/Chat.fxml"));
        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setTitle("New Window");
        primaryStage.setScene(scene);
        primaryStage.initModality(Modality.WINDOW_MODAL);
        primaryStage.initOwner(button.getScene().getWindow());
        primaryStage.show();
    }

    @FXML
    protected void hyperlink() throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://github.com/Shepega-Andrey-Mikhailovich-Team"));
    }

    private void fireEvent() {
        if (!this.textbox.getText().isEmpty())
            ChatClientMain.EVENT_BUS.unsafeFireAndForget(new PlayerJoinEvent(this.textbox.getText()));
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
}