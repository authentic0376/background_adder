import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
class FileProcessor {

    private BufferedImage originalImage;
    private BufferedImage processedImage;

    public void processFile(File file) throws IOException {
        String extension = getFileExtension(file);
        if (!extension.equals("png") && !extension.equals("svg")) {
            throw new IllegalArgumentException("Unsupported file type: " + extension);
        }

        originalImage = ImageIO.read(file);
        processedImage = addWhiteBackground(originalImage);
    }

    public BufferedImage addWhiteBackground(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, newImage.getWidth(), newImage.getHeight());

        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        double imageAspectRatio = (double) imageWidth / imageHeight;

        int panelWidth = newImage.getWidth();
        int panelHeight = newImage.getHeight();
        double panelAspectRatio = (double) panelWidth / panelHeight;

        int targetWidth;
        int targetHeight;

        if (imageAspectRatio > panelAspectRatio) {
            targetWidth = panelWidth;
            targetHeight = (int) (panelWidth / imageAspectRatio);
        } else {
            targetHeight = panelHeight;
            targetWidth = (int) (panelHeight * imageAspectRatio);
        }

        int x = (panelWidth - targetWidth) / 2;
        int y = (panelHeight - targetHeight) / 2;

        g.drawImage(image, x, y, targetWidth, targetHeight, null);
        g.dispose();
        return newImage;
    }

    public void saveProcessedImage(JLabel previewLabel) {
        if (processedImage != null) {
            try {
                File outputFile = new File("C:\\Users\\sprai\\Downloads\\processed_image.png");
                ImageIO.write(processedImage, "png", outputFile);
                JOptionPane.showMessageDialog(previewLabel, "Image saved successfully to " + outputFile.getAbsolutePath());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(previewLabel, "Error saving image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(previewLabel, "No processed image available!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        return name.substring(name.lastIndexOf(".") + 1).toLowerCase();
    }

    public BufferedImage getOriginalImage() {
        return originalImage;
    }

    public BufferedImage getProcessedImage() {
        return processedImage;
    }
}