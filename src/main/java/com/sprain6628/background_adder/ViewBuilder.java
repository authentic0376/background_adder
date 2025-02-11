package com.sprain6628.background_adder;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.util.Builder;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewBuilder implements Builder<Region> {

    private final Model model;
    private final ControlCallback callback;
    private static final Logger LOGGER = Logger.getLogger(ViewBuilder.class.getName());

    public ViewBuilder(Model model, ControlCallback callback) {
        this.model = model;
        this.callback = callback;
    }

    @Override
    public Region build() {
        BorderPane root = createBorderPane();

        SplitPane splitPane = createSplitPane();

        StackPane leftStackPane = createStackPane();
        StackPane rightStackPane = createStackPane();

        Pane leftPane = createLeftPane();
        Pane rightPane = createRightPane();

        leftStackPane.getChildren().add(leftPane);
        rightStackPane.getChildren().add(rightPane);

        splitPane.getItems().addAll(leftStackPane, rightStackPane);

        root.setCenter(splitPane);

        HBox bottomBox = createBottomBox();
        Button saveButton = createSaveButton();

        bottomBox.getChildren().add(saveButton);
        root.setBottom(bottomBox);
        return root;
    }

    private Pane createRightPane() {
        Pane pane = new Pane();
        pane.backgroundProperty().bind(Bindings.createObjectBinding(
                        () -> {
                            Image image = model.getProcessedImageProperty();
                            LOGGER.log(Level.FINE, String.format("Right Panel Bind Background with image: %s", image));
                            return createBackground(image);
                        },
                        model.processedImageProperty() // Observable
                )
        );

        return pane;
    }

    private Pane createLeftPane() {
        Pane pane = new Pane();
        pane.backgroundProperty().bind(Bindings.createObjectBinding(
                        () -> {
                            Image image = model.getOriginalImageProperty();
                            LOGGER.log(Level.FINE, String.format("Left Panel Bind Background with image: %s", image));
                            return createBackground(image);
                        },
                        model.originalImageProperty() // Observable
                )
        );

        setDragOver(pane);
        setDragDropped(pane);
        return pane;
    }

    private Button createSaveButton() {
        Button saveButton = new Button("Save");
        saveButton.getStyleClass().add("save-button");
        saveButton.setOnAction(
                (e) -> {
                    this.callback.save();
                }
        );
        return saveButton;
    }

    private static HBox createBottomBox() {
        HBox bottomBox = new HBox();
        bottomBox.setAlignment(javafx.geometry.Pos.BOTTOM_RIGHT);
        bottomBox.getStyleClass().add("bottom-box");
        return bottomBox;
    }

    private static StackPane createStackPane() {
        StackPane leftStackPane = new StackPane();
        leftStackPane.setPrefSize(40, 40);
        leftStackPane.getStyleClass().add("checkerboard-pattern");
        return leftStackPane;
    }

    private static SplitPane createSplitPane() {
        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPositions(0.5);
        return splitPane;
    }

    private static BorderPane createBorderPane() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-padding: 10px;");
        return root;
    }

    private void setDragDropped(Pane pane) {
        pane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasFiles()) {
                List<File> files = db.getFiles();

                File file = files.get(0);

                if (isImageFile(file)) {
                    model.setDroppedFileProperty(file);
                    success = true;
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });

    }

    private boolean isImageFile(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".png") || fileName.endsWith(".jpg") ||
                fileName.endsWith(".jpeg") || fileName.endsWith(".gif") ||
                fileName.endsWith(".bmp") || fileName.endsWith(".svg");
    }

    private void setDragOver(Pane pane) {
        pane.setOnDragOver(event -> {
            if (event.getGestureSource() != pane && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
    }

    private Background createBackground(Image image) {

        if (image == null) {
            return Background.EMPTY;
        }

        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, false)
        );
        return new Background(backgroundImage);
    }
}
