package com.sprain6628.background_adder;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sprain6628.background_adder.config.CustomModule;
import com.sprain6628.background_adder.config.Config;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class BackgroundAdderApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        Injector injector = Guice.createInjector(new CustomModule());
        Controller controller = injector.getInstance(Controller.class);
        Scene scene = new Scene(controller.getView(), 900, 600);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Background Adder");
        primaryStage.getIcons().add(
                new Image(
                        Objects.requireNonNull(
                                getClass().getResourceAsStream("/image/background adder.png") // .ico doesn't work
                        )
                )
        );
        primaryStage.show();
    }

    public static void main(String[] args) {
        Config.setupLogging();
        launch(args);
    }
}