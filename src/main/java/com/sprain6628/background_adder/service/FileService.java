package com.sprain6628.background_adder.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileService {

    private String originalFileName;


    public File save(File tempFile) throws IOException {
        if (tempFile == null) return null;
        Path targetPath = createTargetPath();
        Files.copy(tempFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        return targetPath.toFile();
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
}
