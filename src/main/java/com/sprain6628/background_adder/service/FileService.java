package com.sprain6628.background_adder.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileService {

    private String originalFileName;


    public void save(File tempFile) {

        Path targetPath = createTargetPath();

        try {
            Files.copy(tempFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getFileExtension(File file) {
        String name = file.getName();
        if (hasExtension(name))
            return splitFileName(name)[1];
        return "";
    }

    public void setOriginalFileName(String name) {
        originalFileName = name;
    }

    private Path createTargetPath() {

        Path downloadFolder = Paths.get(System.getProperty("user.home"), "Downloads");
        String fileName = createNewName();

        return downloadFolder.resolve(fileName);
    }

    private String createNewName() {
        int lastDotIndex = originalFileName.lastIndexOf(".");
        StringBuilder fileName = new StringBuilder(originalFileName);
        String suffix = "_bg";

        if (lastDotIndex < 0) {
            fileName.append(suffix);
        }

        fileName.insert(lastDotIndex, suffix);
        return fileName.toString();
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

    private boolean hasExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        return lastDotIndex > 0 && lastDotIndex < fileName.length() - 1;
    }
}
