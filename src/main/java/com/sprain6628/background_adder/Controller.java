package com.sprain6628.background_adder;

import com.sprain6628.background_adder.file_convert_strategies.ConvertStrategy;
import com.sprain6628.background_adder.file_convert_strategies.PngConverter;
import javafx.beans.binding.Bindings;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller {


    private final ViewBuilder viewBuilder;
    private final Model model;
    private final Map<String, ConvertStrategy> convertStrategyMap;

    public Controller() {
        this.model = new Model();
        viewBuilder = new ViewBuilder(model);
        convertStrategyMap = new HashMap<>();

        initConverterStrategyMap();
        bindImageToFile();
    }

    private void initConverterStrategyMap() {
        List<ConvertStrategy> convertStrategies = List.of(new PngConverter());
        for (ConvertStrategy strategy : convertStrategies) {
            convertStrategyMap.put(strategy.getExtension(), strategy);
        }
    }

    public Region getView() {
        return viewBuilder.build();
    }

    private void bindImageToFile() {

        model.originalImageProperty().bind(
                Bindings.createObjectBinding(
                        () -> convert(model.getDroppedFileProperty()),
                        model.droppedFileProperty()
                )
        );
    }

    private Image convert(File file) {
        ConvertStrategy strategy = resolveStrategy(file);
        return strategy.convert(file);
    }

    private ConvertStrategy resolveStrategy(File file) {
        String extension = getFileExtension(file);
        return convertStrategyMap.get(extension);
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


}