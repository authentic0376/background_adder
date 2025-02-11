package com.sprain6628.background_adder;

import javafx.beans.binding.Bindings;
import javafx.scene.layout.Region;

import java.io.File;
import java.util.logging.Logger;

public class Controller implements ControlCallback {
    private final ViewBuilder viewBuilder;
    private final Model model;
    private final Interactor interactor;
    private static final Logger LOGGER = Logger.getLogger(Controller.class.getName());

    public Controller() {
        this.model = new Model();
        viewBuilder = new ViewBuilder(model, this);
        interactor = new Interactor();

        bind();
    }

    public Region getView() {
        return viewBuilder.build();
    }

    private void bind() {

        // Dropped File -> Original Image
        model.originalImageProperty().bind(
                Bindings.createObjectBinding(
                        () -> {
                            File droppedFile = model.getDroppedFileProperty();
                            if (droppedFile == null) return null;

                            interactor.setOriginalFileName(droppedFile);
                            return interactor.convert(droppedFile);
                        },
                        model.droppedFileProperty()
                )
        );

        // Dropped File -> Processed File
        model.processedFileProperty().bind(
                Bindings.createObjectBinding(
                        () -> interactor.addBackground(model.getDroppedFileProperty()),
                        model.droppedFileProperty()
                )
        );

        // Processed File -> Processed Image
        model.processedImageProperty().bind(
                Bindings.createObjectBinding(
                        () -> interactor.convert(model.getProcessedFileProperty()),
                        model.processedFileProperty()
                )
        );
    }

    @Override
    public void save() {
        File tempFile = model.getProcessedFileProperty();
        interactor.save(tempFile);
    }
}