package com.sprain6628.background_adder;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sprain6628.background_adder.model.ExceptionModel;
import com.sprain6628.background_adder.service.FileService;
import com.sprain6628.background_adder.service.ImageService;
import com.sprain6628.background_adder.service.PngService;
import com.sprain6628.background_adder.service.SvgService;
import com.sprain6628.background_adder.util.FileUtil;
import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class Interactor {
    private final Map<String, ImageService> imageServiceMap;
    private final FileService fileService;
    private final ExceptionModel exceptionModel;
    private static final Logger LOGGER = Logger.getLogger(Interactor.class.getName());

    @Inject
    public Interactor(ExceptionModel exceptionModel, FileService fileService) {
        this.fileService = fileService;
        this.exceptionModel = exceptionModel;

        imageServiceMap = new HashMap<>();
        initImageServiceMap();
    }

    public File save(File tempFile) {
        try {
            return fileService.save(tempFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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


    public File addBackground(File file) {
        LOGGER.log(Level.FINE, "Start Add Background");
        if (file == null) {
            LOGGER.log(Level.FINE, "End Add Background No File");
            return null;
        }
        ImageService service = resolveImageService(file);
        LOGGER.log(Level.FINE, "End Add Background");
        return service.addBackground(file);
    }

    public Image convert(File file) {
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
        String extension = FileUtil.getFileExtension(file);
        return imageServiceMap.get(extension);
    }

    public void setOriginalFileName(File droppedFile) {
        fileService.setOriginalFileName(droppedFile.getName());
    }
}
