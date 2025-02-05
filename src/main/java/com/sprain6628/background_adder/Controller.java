package com.sprain6628.background_adder;

import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.Objects;

public class Controller {

    @FXML
    private StackPane leftPane;
    @FXML
    private StackPane rightPane;
    public void initialize() {
        addImagePaneToStackPane(leftPane, "sample.jpg");
        addImagePaneToStackPane(rightPane, "sample.jpg");
    }
    private void addImagePaneToStackPane(StackPane stackPane, String resourceString){

        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(resourceString)));
        // 새로운 Pane 생성
        Pane newPane = new Pane();

        // 배경 이미지 설정
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, false)
        );

        newPane.setBackground(new Background(background));

        // stackPane에 추가
        stackPane.getChildren().add(newPane);
    }
}