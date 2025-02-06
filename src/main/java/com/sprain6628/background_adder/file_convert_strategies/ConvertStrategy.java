package com.sprain6628.background_adder.file_convert_strategies;

import javafx.scene.image.Image;

import java.io.File;

public interface ConvertStrategy {
    Image convert(File file);

    String getExtension();
}
