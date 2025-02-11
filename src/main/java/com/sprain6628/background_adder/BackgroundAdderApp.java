package com.sprain6628.background_adder;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BackgroundAdderApp extends Application {
    private static final Logger LOGGER = Logger.getLogger(BackgroundAdderApp.class.getName());

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(new Controller().getView(), 900, 600);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Background Adder");
        primaryStage.show();
    }

    public static void main(String[] args) {
        LoggingConfig.setupLogging();

        try {
            Class.forName("org.w3c.dom.css.DOMImplementationCSS");
            LOGGER.log(Level.FINE, "클래스 로드 성공!");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.FINE, "클래스 로드 실패: " + e.getMessage());
        }

        launch(args);
    }
}