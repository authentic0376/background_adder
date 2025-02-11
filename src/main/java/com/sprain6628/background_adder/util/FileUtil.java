package com.sprain6628.background_adder.util;

import java.io.File;

public class FileUtil {
    public static String getFileExtension(File file) {
        String name = file.getName();
        if (hasExtension(name))
            return splitFileName(name)[1];
        return "";
    }

    private static String[] splitFileName(String name) {
        if (!hasExtension(name)) {
            return new String[]{name, ""};
        }

        int lastDotIndex = name.lastIndexOf(".");
        return new String[]{
                name.substring(0, lastDotIndex),
                name.substring(lastDotIndex + 1).toLowerCase()
        };
    }

    private static boolean hasExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        return lastDotIndex > 0 && lastDotIndex < fileName.length() - 1;
    }
}
