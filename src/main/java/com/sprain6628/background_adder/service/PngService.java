package com.sprain6628.background_adder.service;

import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PngService implements ImageService {
    private static final Logger LOGGER = Logger.getLogger(PngService.class.getName());

    @Override
    public Image convert(File file) {
        return new Image(file.toURI().toString());
    }

    @Override
    public String getExtension() {
        return "png";
    }

    @Override
    public File addBackground(File file) throws Exception {
        BufferedImage originalImage = null;
        try {
            originalImage = readImage(file);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Exception while read image");
            throw e;
        }
        BufferedImage processedImage = addWhiteBackground(originalImage);

        File tempFile = null;
        try {
            tempFile = File.createTempFile("temp_image_", "." + getExtension());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Exception while create temp file");
            throw e;
        }

        try {
            ImageIO.write(processedImage, getExtension(), tempFile);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Exception while write image");
            throw e;
        }

        tempFile.deleteOnExit();

        return tempFile;

    }

    private BufferedImage addWhiteBackground(BufferedImage originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = newImage.createGraphics();
        try {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);
            g.drawImage(originalImage, 0, 0, width, height, null);
        } finally {
            g.dispose();
        }
        return newImage;
    }

    private BufferedImage readImage(File file) throws IOException {
        return ImageIO.read(file);
    }
}
