package me.chat.dialog;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.chat.common.IOHelper;

import java.io.IOException;

public class LoginDialog extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        // Видаляємо window decoration
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initStyle(StageStyle.TRANSPARENT);

        FXMLLoader fxmlLoader = new FXMLLoader(IOHelper.getResourceURL("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 360, Color.TRANSPARENT);
        scene.getStylesheets().add(IOHelper.getResourceURL("login.css").toExternalForm());

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
}