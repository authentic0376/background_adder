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
        validateFileExtension(file);
        originalImage = ImageIO.read(file);
        processedImage = createImageWithWhiteBackground(originalImage);
    }

    private void validateFileExtension(File file) {
        String extension = getFileExtension(file);
        if (!extension.equals("png") && !extension.equals("svg")) {
            throw new IllegalArgumentException("Unsupported file type: " + extension);
        }
    }

    private BufferedImage createImageWithWhiteBackground(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = newImage.createGraphics();
        try {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);
            g.drawImage(image, 0, 0, width, height, null);
        } finally {
            g.dispose();
        }

        return newImage;
    }

    public void saveProcessedImage(JLabel previewLabel) {
        if (processedImage == null) {
            JOptionPane.showMessageDialog(previewLabel, "No processed image available!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String userHome = System.getProperty("user.home");
            String downloadFolder = userHome + File.separator + "Downloads";
            File outputFile = new File(downloadFolder, "processed_image.png");

            ImageIO.write(processedImage, "png", outputFile);
            JOptionPane.showMessageDialog(previewLabel, "Image saved successfully to " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(previewLabel, "Error saving image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        if (hasExtension(name)) {
            throw new IllegalArgumentException("File does not have a valid extension: " + name);
        }
        return _getFileExtension(name);
    }

    private boolean hasExtension(String name) {
        int lastDotIndex = name.lastIndexOf(".");
        return lastDotIndex == -1 || lastDotIndex == name.length() - 1;
    }

    private String _getFileExtension(String name) {
        int lastDotIndex = name.lastIndexOf(".");
        return name.substring(lastDotIndex + 1).toLowerCase();
    }

    public BufferedImage getOriginalImage() {
        return originalImage;
    }

    public BufferedImage getProcessedImage() {
        return processedImage;
    }
}
