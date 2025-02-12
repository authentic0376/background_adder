package com.sprain6628.background_adder.service;

import javafx.scene.image.Image;

import java.io.File;

public interface ImageService {
    Image convert(File file) throws Exception;

    File addBackground(File file) throws Exception;

    String getExtension();
}
