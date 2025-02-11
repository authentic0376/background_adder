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
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller implements ControlCallback {
    private final ViewBuilder viewBuilder;
    private final Model model;
    private final Map<String, ImageService> imageServiceMap;
    private final FileService fileService;
    private static final Logger LOGGER = Logger.getLogger(Controller.class.getName());

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

        // Dropped File -> Original Image
        model.originalImageProperty().bind(
                Bindings.createObjectBinding(
                        () -> convert(model.getDroppedFileProperty()),
                        model.droppedFileProperty()
                )
        );

        // Dropped File -> Processed File
        model.processedFileProperty().bind(
                Bindings.createObjectBinding(
                        () -> addBackground(model.getDroppedFileProperty()),
                        model.droppedFileProperty()
                )
        );

        // Processed File -> Processed Image
        model.processedImageProperty().bind(
                Bindings.createObjectBinding(
                        () -> convert(model.getProcessedFileProperty()),
                        model.processedFileProperty()
                )
        );
    }

    private File addBackground(File file) {
        LOGGER.log(Level.FINE, "Start Add Background");
        if (file == null) {
            LOGGER.log(Level.FINE, "End Add Background No File");
            return null;
        }
        ImageService service = resolveImageService(file);
        LOGGER.log(Level.FINE, "End Add Background");
        return service.addBackground(file);
    }

    private Image convert(File file) {
        LOGGER.log(Level.FINE, "Start Convert");
        if (file == null) {
            LOGGER.log(Level.FINE, "End Convert No File");
            return null;
        }
        ImageService service = resolveImageService(file);
        LOGGER.log(Level.FINE, "End Convert");
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