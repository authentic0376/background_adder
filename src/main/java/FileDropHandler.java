import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.swing.*;

class FileDropHandler extends TransferHandler {

    private final JPanel panel;
    private final FileProcessor fileProcessor;
    private final JLabel originalPreview;
    private final JLabel processedPreview;

    public FileDropHandler(JPanel panel, FileProcessor fileProcessor, JLabel originalPreview, JLabel processedPreview) {
        this.panel = panel;
        this.fileProcessor = fileProcessor;
        this.originalPreview = originalPreview;
        this.processedPreview = processedPreview;

        initializeDropTarget();
    }

    private void initializeDropTarget() {
        new DropTarget(panel, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                handleDropEvent(dtde);
            }
        });
    }

    private void handleDropEvent(DropTargetDropEvent dtde) {
        try {
            dtde.acceptDrop(DnDConstants.ACTION_COPY);
            List<File> droppedFiles = extractDroppedFiles(dtde);
            File file = validateAndFetchFile(droppedFiles);

            fileProcessor.processFile(file);
            updatePreviewImages();
        } catch (Exception e) {
            showErrorDialog(e);
        }
    }

    private List<File> extractDroppedFiles(DropTargetDropEvent dtde) throws Exception {
        return (List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
    }

    private File validateAndFetchFile(List<File> droppedFiles) {
        if (droppedFiles.isEmpty()) {
            throw new IllegalArgumentException("No file was dropped.");
        }
        return droppedFiles.get(0);
    }

    private void updatePreviewImages() {
        BufferedImage original = fileProcessor.getOriginalImage();
        ImageIcon originalIcon = scaleImageToLabel(original, originalPreview);
        originalPreview.setIcon(originalIcon);
        originalPreview.setText(null);

        BufferedImage processed = fileProcessor.getProcessedImage();
        ImageIcon processedIcon = scaleImageToLabel(processed, processedPreview);
        processedPreview.setIcon(processedIcon);
        processedPreview.setText(null);
    }

    private ImageIcon scaleImageToLabel(BufferedImage image, JLabel label) {
        double imageAspectRatio = (double) image.getWidth() / image.getHeight();
        int panelWidth = label.getWidth();
        int panelHeight = label.getHeight();
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

        return new ImageIcon(image.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH));
    }

    private void showErrorDialog(Exception e) {
        JOptionPane.showMessageDialog(panel, "Error processing file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
