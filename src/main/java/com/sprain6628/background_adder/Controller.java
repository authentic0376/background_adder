package com.sprain6628.background_adder;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sprain6628.background_adder.model.ExceptionModel;
import com.sprain6628.background_adder.model.ImageModel;
import javafx.beans.binding.Bindings;
import javafx.scene.layout.Region;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

@Singleton
public class Controller implements ControlCallback {
    private final ViewBuilder viewBuilder;
    private final ImageModel imageModel;
    private final ExceptionModel exceptionModel;
    private final Interactor interactor;
    private static final Logger LOGGER = Logger.getLogger(Controller.class.getName());

    @Inject
    public Controller(ImageModel imageModel, ExceptionModel exceptionModel, ViewBuilder viewBuilder, Interactor interactor) {

        this.imageModel = imageModel;
        this.exceptionModel = exceptionModel;
        this.viewBuilder = viewBuilder;
        this.interactor = interactor;

        bind();
    }

    public Region getView() {
        return viewBuilder.build();
    }

    private void bind() {

        // Dropped File -> Original Image
        imageModel.originalImageProperty().bind(
                Bindings.createObjectBinding(
                        () -> {
                            File droppedFile = imageModel.getDroppedFile();
                            if (droppedFile == null) return null;

                            interactor.setOriginalFileName(droppedFile);
                            return interactor.convert(droppedFile);
                        },
                        imageModel.droppedFileProperty()
                )
        );

        // Dropped File -> Processed File
        imageModel.processedFileProperty().bind(
                Bindings.createObjectBinding(
                        () -> interactor.addBackground(imageModel.getDroppedFile()),
                        imageModel.droppedFileProperty()
                )
        );

        // Processed File -> Processed Image
        imageModel.processedImageProperty().bind(
                Bindings.createObjectBinding(
                        () -> interactor.convert(imageModel.getProcessedFile()),
                        imageModel.processedFileProperty()
                )
        );
    }

    @Override
    public void save() throws IOException {
        File tempFile = imageModel.getProcessedFile();
        if (tempFile == null)
            throw new FileNotFoundException();
        interactor.save(tempFile);
    }
}