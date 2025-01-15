import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

class FileProcessor {

    private BufferedImage originalImage;
    private BufferedImage processedImage;
    private FileHandlerStrategy strategy;

    private final Map<String, FileHandlerStrategy> strategyMap;

    public FileProcessor(Map<String, FileHandlerStrategy> strategyMap) {
        this.strategyMap = strategyMap;
    }

    public void processFile(File file) throws IOException {
        String extension = getFileExtension(file);
        strategy = strategyMap.get(extension);

        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported file type: " + extension);
        }
        originalImage = strategy.readImage(file);
        processedImage = strategy.addWhiteBackground(originalImage);
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

            strategy.write(outputFile);
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
