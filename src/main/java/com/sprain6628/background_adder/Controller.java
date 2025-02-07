package com.sprain6628.background_adder;

import com.sprain6628.background_adder.service.ImageService;
import com.sprain6628.background_adder.service.PngService;
import javafx.beans.binding.Bindings;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller implements ControlCallback{
    private final ViewBuilder viewBuilder;
    private final Model model;
    private final Map<String, ImageService> imageServiceMap;

    public Controller() {
        this.model = new Model();
        viewBuilder = new ViewBuilder(model, this);
        imageServiceMap = new HashMap<>();

        initImageServiceMap();
        bind();
    }

    private void initImageServiceMap() {
        List<ImageService> imageServices = List.of(new PngService());

        for (ImageService service : imageServices) {
            imageServiceMap.put(service.getExtension(), service);
        }
    }

    public Region getView() {
        return viewBuilder.build();
    }

    private void bind() {

        model.originalImageProperty().bind(
                Bindings.createObjectBinding(
                        () -> convert(model.getDroppedFileProperty()),
                        model.droppedFileProperty()
                )
        );

        model.processedFileProperty().bind(
                Bindings.createObjectBinding(
                        () -> addBackground(model.getDroppedFileProperty()),
                        model.droppedFileProperty()
                )
        );
        model.processedImageProperty().bind(
                Bindings.createObjectBinding(
                        () -> convert(model.getProcessedFileProperty()),
                        model.processedFileProperty()
                )
        );
    }

    private File addBackground(File file) {
        if (file == null)
            return null;
        ImageService service = resolveImageService(file);
        return service.addBackground(file);
    }

    private Image convert(File file) {
        if (file == null)
            return null;
        ImageService service = resolveImageService(file);
        return service.convert(file);
    }

    private ImageService resolveImageService(File file) {
        String extension = getFileExtension(file);
        return imageServiceMap.get(extension);
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        if (!hasExtension(name)) {
            throw new IllegalArgumentException("File does not have a valid extension: " + name);
        }
        return _getFileExtension(name);
    }

    private boolean hasExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        return lastDotIndex > 0 && lastDotIndex < fileName.length() - 1;
    }

    private String _getFileExtension(String name) {
        int lastDotIndex = name.lastIndexOf(".");
        return name.substring(lastDotIndex + 1).toLowerCase();
    }

    @Override
    public void save() {
        File tempFile = model.getProcessedFileProperty();

        Path targetPath = createTargetPath(tempFile);

        try {
            if (Files.exists(targetPath)) {
                Files.delete(targetPath);
            }
            Files.move(tempFile.toPath(), targetPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Path createTargetPath(File tempFile) {
        ImageService service = resolveImageService(tempFile);
        String extension = service.getExtension();

        Path downloadFolder = Paths.get(System.getProperty("user.home"), "Downloads");
        String fileName = String.format("processed_image.%s", extension);

        return downloadFolder.resolve(fileName);
    }

}