package com.sprain6628.background_adder.model;

import jakarta.inject.Singleton;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;

import java.io.File;

@Singleton
public class ImageModel {
    private final ObjectProperty<File> droppedFile = new SimpleObjectProperty<>();
    private final ObjectProperty<File> processedFile = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> originalImage = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> processedImage = new SimpleObjectProperty<>();

    public File getDroppedFile() {
        return droppedFile.get();
    }

    public ObjectProperty<File> droppedFileProperty() {
        return droppedFile;
    }

    public void setDroppedFile(File droppedFile) {
        this.droppedFile.set(droppedFile);
    }

    public File getProcessedFile() {
        return processedFile.get();
    }

    public ObjectProperty<File> processedFileProperty() {
        return processedFile;
    }

    public void setProcessedFile(File processedFile) {
        this.processedFile.set(processedFile);
    }

    public Image getOriginalImage() {
        return originalImage.get();
    }

    public ObjectProperty<Image> originalImageProperty() {
        return originalImage;
    }

    public void setOriginalImage(Image originalImage) {
        this.originalImage.set(originalImage);
    }

    public Image getProcessedImage() {
        return processedImage.get();
    }

    public ObjectProperty<Image> processedImageProperty() {
        return processedImage;
    }

    public void setProcessedImage(Image processedImage) {
        this.processedImage.set(processedImage);
    }
}
