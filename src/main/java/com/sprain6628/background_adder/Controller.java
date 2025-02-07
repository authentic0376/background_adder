package com.sprain6628.background_adder;

import com.sprain6628.background_adder.service.FileService;
import com.sprain6628.background_adder.service.ImageService;
import com.sprain6628.background_adder.service.PngService;
import com.sprain6628.background_adder.service.SvgService;
import javafx.beans.binding.Bindings;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller implements ControlCallback {
    private final ViewBuilder viewBuilder;
    private final Model model;
    private final Map<String, ImageService> imageServiceMap;
    private final FileService fileService;

    public Controller() {
        this.model = new Model();
        viewBuilder = new ViewBuilder(model, this);
        imageServiceMap = new HashMap<>();
        fileService = new FileService();

        initImageServiceMap();
        bind();
    }

    private void initImageServiceMap() {
        List<ImageService> imageServices = List.of(
                new PngService()
                , new SvgService()
        );

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
        String extension = fileService.getFileExtension(file);
        return imageServiceMap.get(extension);
    }


    @Override
    public void save() {
        File originalFile = model.getDroppedFileProperty();
        File tempFile = model.getProcessedFileProperty();
        fileService.save(tempFile, originalFile);
    }
}