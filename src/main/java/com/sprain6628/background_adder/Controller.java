package com.sprain6628.background_adder;

import javafx.scene.layout.Region;

import java.net.URISyntaxException;

public class Controller {


    private final ViewBuilder viewBuilder;

    public Controller() {
        viewBuilder = new ViewBuilder();
    }

    public Region getView() {
        return viewBuilder.build();
    }

}