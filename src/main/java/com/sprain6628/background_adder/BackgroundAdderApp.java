package com.sprain6628.background_adder;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class BackgroundAdderApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(new Controller().getView(), 900, 600);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Background Adder");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}