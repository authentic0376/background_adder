package com.sprain6628.background_adder.service;

import javafx.scene.image.Image;

import java.io.File;

public class SvgService implements ImageService {
    @Override
    public Image convert(File file) {
        return new Image(file.toURI().toString());
    }

    @Override
    public File addBackground(File file) {
        return null;
    }

    @Override
    public String getExtension() {
        return "svg";
    }
}
