package com.sprain6628.background_adder;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

public class Controller {

    @FXML
    private Pane leftPane;
    @FXML
    private Pane rightPane;

    public void initialize() throws URISyntaxException {
        setLeftPaneDragOver();
        setLeftPaneDragDropped();

        File sample = new File(Objects.requireNonNull(getClass().getResource("sample.jpg")).toURI());
        addImagePaneToPane(leftPane, sample);
        addImagePaneToPane(rightPane, sample);
    }

    private void setLeftPaneDragDropped(){
        leftPane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasFiles()) {
                List<File> files = db.getFiles();

                // 하나의 파일만 처리
                File file = files.get(0);

                // 확장자 검사 (이미지 파일인지 확인)
                if (isImageFile(file)) {
                    addImagePaneToPane(leftPane, file);
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

    private void setLeftPaneDragOver() {
        leftPane.setOnDragOver(event -> {
            if (event.getGestureSource() != leftPane && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
    }

    private void addImagePaneToPane(Pane pane, File imgFile) {

        Image backgroundImage = new Image(imgFile.toURI().toString());

        // 배경 이미지 설정
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, false)
        );

        pane.setBackground(new Background(background));
    }
}