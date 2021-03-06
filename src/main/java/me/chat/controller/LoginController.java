package me.chat.controller;

import com.jfoenix.controls.JFXRippler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.chat.ChatClientMain;
import me.chat.common.VerifyHelper;
import me.chat.connection.impl.ChatConnection;
import me.chat.dialog.effects.ShakeTransition;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @Setter
    @Getter
    private static LoginController instance;

    @FXML
    private TextField textbox;

    @FXML
    private Button button;

    @Getter
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
        if (!this.textbox.getText().isEmpty() && VerifyHelper.isValidUsername(this.textbox.getText())) {
            ChatConnection chatConnection = ChatClientMain.getChatConnection();
            chatConnection.setUserName(this.textbox.getText().trim());
            chatConnection.join();
            return;
        }

        ShakeTransition shakeTransition = new ShakeTransition(this.button);
        shakeTransition.playFromStart();
    }

    @FXML
    protected void onExit() {
        ChatClientMain.getChatConnection().leave(true);
        Platform.exit();
    }

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // ???????????????????? Ripple ?????????? ???? ????????????
        JFXRippler jfxRippler = new JFXRippler(this.button);
        jfxRippler.setRipplerFill(Paint.valueOf("#FDEBAE"));
        jfxRippler.setLayoutX(99);
        jfxRippler.setLayoutY(228);
        pane.getChildren().add(jfxRippler);

        LoginController.setupInstance(this);
    }

    public static void setupInstance(LoginController instance) {
        if (instance == null)
            throw new RuntimeException("Instance cannot be null!");

        if (LoginController.getInstance() != null)
            throw new RuntimeException("Instance already set!");

        LoginController.setInstance(instance);
    }
}