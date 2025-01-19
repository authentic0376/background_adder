import javax.swing.*;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

class FileProcessor {
    private FileHandlerStrategy strategy;
    private String extension;
    private Object originalImage;
    private Object processedImage;
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
        Object[] images = strategy.processFile(file);
        originalImage = images[0];
        processedImage = images[1];
    }


    public void saveProcessedImage(JLabel previewLabel) {
        if (processedImage == null) {
            JOptionPane.showMessageDialog(previewLabel, "No processed image available!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String userHome = System.getProperty("user.home");
            String downloadFolder = userHome + File.separator + "Downloads";
            String fileName = "processed_image";

            Path outputPath = Paths.get(downloadFolder, fileName);
            strategy.write(processedImage, outputPath);
            JOptionPane.showMessageDialog(previewLabel, "Image saved successfully to " + outputPath);
        } catch (IOException | TransformerException e) {
            JOptionPane.showMessageDialog(previewLabel, "Error saving image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        if (hasExtension(name)) {
            throw new IllegalArgumentException("File does not have a valid extension: " + name);
        }
        extension = _getFileExtension(name);
        return extension;
    }

    private boolean hasExtension(String name) {
        int lastDotIndex = name.lastIndexOf(".");
        return lastDotIndex == -1 || lastDotIndex == name.length() - 1;
    }

    private String _getFileExtension(String name) {
        int lastDotIndex = name.lastIndexOf(".");
        return name.substring(lastDotIndex + 1).toLowerCase();
    }

    public String getExtension() {
        return extension;
    }

    public Object getOriginalImage() {
        return originalImage;
    }

    public Object getProcessedImage() {
        return processedImage;
    }
}
