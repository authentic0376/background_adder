import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class PngHandler implements FileHandlerStrategy {
    @Override
    public Object[] processFile(File file) throws IOException {
        BufferedImage originalImage = readImage(file);
        BufferedImage processedImage = addWhiteBackground(originalImage);
        return new BufferedImage[]{originalImage, processedImage};
    }

    public BufferedImage addWhiteBackground(BufferedImage originalImage) {
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

    public BufferedImage readImage(File file) throws IOException {
        return ImageIO.read(file);
    }

    @Override
    public void write(Object processedImage, File outputFile) throws IOException {
        ImageIO.write((BufferedImage) processedImage, "png", outputFile);
    }
}

