import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

class FileDropHandler extends TransferHandler {
    public FileDropHandler(JPanel panel, FileProcessor fileProcessor, JLabel originalPreview, JLabel processedPreview) {
        new DropTarget(panel, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    List<File> droppedFiles = (List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    File file = droppedFiles.get(0);
                    fileProcessor.processFile(file);

                    BufferedImage original = fileProcessor.getOriginalImage();
                    double imageAspectRatio = (double) original.getWidth() / original.getHeight();

                    int panelWidth = originalPreview.getWidth();
                    int panelHeight = originalPreview.getHeight();
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

                    originalPreview.setText(null);
                    processedPreview.setText(null);
                    originalPreview.setIcon(new ImageIcon(original.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH)));
                    processedPreview.setIcon(new ImageIcon(fileProcessor.getProcessedImage().getScaledInstance(
                            processedPreview.getWidth(), processedPreview.getHeight(), Image.SCALE_SMOOTH)));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(panel, "Error processing file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
