package org.openjfx.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/app.fxml"));
        var scene = new Scene(fxmlLoader.load());

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void run(String[] args) {
        launch();
    }

}