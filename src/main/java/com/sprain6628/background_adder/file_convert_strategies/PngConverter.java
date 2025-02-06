package com.sprain6628.background_adder.file_convert_strategies;

import javafx.scene.image.Image;

import java.io.File;

public class PngConverter implements ConvertStrategy{
    @Override
    public Image convert(File file) {
        return new Image(file.toURI().toString());
    }

    @Override
    public String getExtension() {
        return "png";
    }
}
