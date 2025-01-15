import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PngHandler implements FileHandlerStrategy {

    private BufferedImage processedImage;

    @Override
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

        processedImage = newImage;
        return newImage;
    }

    @Override
    public BufferedImage readImage(File file) throws IOException {
        return ImageIO.read(file);
    }

    @Override
    public void write(File outputFile) throws IOException {
        ImageIO.write(processedImage, "png", outputFile);
    }
}

