package com.sprain6628.background_adder.service;

import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PngService implements ImageService {
    @Override
    public Image convert(File file) {
        return new Image(file.toURI().toString());
    }

    @Override
    public String getExtension() {
        return "png";
    }

    @Override
    public File addBackground(File file) {
        try {
            BufferedImage originalImage = readImage(file);
            BufferedImage processedImage = addWhiteBackground(originalImage);

            File tempFile = File.createTempFile("temp_image_", "." + getExtension());

            ImageIO.write(processedImage, getExtension(), tempFile);

            tempFile.deleteOnExit();

            return tempFile;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
