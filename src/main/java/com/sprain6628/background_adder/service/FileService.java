package com.sprain6628.background_adder.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileService {

    private boolean hasExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        return lastDotIndex > 0 && lastDotIndex < fileName.length() - 1;
    }

    private String[] splitFileName(String name) {
        if (!hasExtension(name)) {
            return new String[]{name, ""};
        }

        int lastDotIndex = name.lastIndexOf(".");
        return new String[]{
                name.substring(0, lastDotIndex),
                name.substring(lastDotIndex + 1).toLowerCase()
        };
    }

    public String getFileExtension(File file) {
        String name = file.getName();
        if (hasExtension(name))
            return splitFileName(name)[1];
        return "";
    }

    private String getNameWithoutExtension(File file) {
        String name = file.getName();
        return splitFileName(name)[0];
    }

    public void save(File tempFile, File originalFile) {

        Path targetPath = createTargetPath(originalFile);

        try {
            if (Files.exists(targetPath)) {
                Files.delete(targetPath);
            }
            Files.move(tempFile.toPath(), targetPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Path createTargetPath(File originalFile) {

        Path downloadFolder = Paths.get(System.getProperty("user.home"), "Downloads");
        String fileNameWithoutExtension = getNameWithoutExtension(originalFile);
        String extension = getFileExtension(originalFile);

        String fileName = String.format("%s_bg.%s", fileNameWithoutExtension, extension);

        return downloadFolder.resolve(fileName);
    }
}
