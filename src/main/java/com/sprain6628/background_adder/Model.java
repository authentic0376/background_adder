package com.sprain6628.background_adder;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;

import java.io.File;

public class Model {
    private final ObjectProperty<File> droppedFileProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<File> processedFileProperty = new SimpleObjectProperty<>();

    private final ObjectProperty<Image> originalImageProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> processedImageProperty = new SimpleObjectProperty<>();

    public File getDroppedFileProperty() {
        return droppedFileProperty.get();
    }

    public ObjectProperty<File> droppedFileProperty() {
        return droppedFileProperty;
    }

    public void setDroppedFileProperty(File droppedFileProperty) {
        this.droppedFileProperty.set(droppedFileProperty);
    }

    public File getProcessedFileProperty() {
        return processedFileProperty.get();
    }

    public ObjectProperty<File> processedFileProperty() {
        return processedFileProperty;
    }

    public void setProcessedFileProperty(File processedFileProperty) {
        this.processedFileProperty.set(processedFileProperty);
    }

    public Image getOriginalImageProperty() {
        return originalImageProperty.get();
    }

    public ObjectProperty<Image> originalImageProperty() {
        return originalImageProperty;
    }

    public void setOriginalImageProperty(Image originalImageProperty) {
        this.originalImageProperty.set(originalImageProperty);
    }

    public Image getProcessedImageProperty() {
        return processedImageProperty.get();
    }

    public ObjectProperty<Image> processedImageProperty() {
        return processedImageProperty;
    }

    public void setProcessedImageProperty(Image processedImageProperty) {
        this.processedImageProperty.set(processedImageProperty);
    }
}
