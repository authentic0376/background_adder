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

public class ViewBuilder implements Builder<Region> {

    private final Model model;

    public ViewBuilder(Model model) {
        this.model = model;
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
                            return createBackground(image);
                        },
                        model.originalImageProperty() // Observable
                )
        );

        setDragOver(pane);
        setDragDropped(pane);
        return pane;
    }

    private static Button createSaveButton() {
        Button saveButton = new Button("Save");
        saveButton.getStyleClass().add("save-button");
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

                // 하나의 파일만 처리
                File file = files.get(0);

                // 확장자 검사 (이미지 파일인지 확인)
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
                fileName.endsWith(".bmp");
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
            return Background.EMPTY; // 이미지 없으면 기본 배경
        }
        // 배경 이미지 설정
        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, false)
        );
        return new Background(backgroundImage);
    }
}
