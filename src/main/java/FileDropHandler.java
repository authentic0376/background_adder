import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;

class FileDropHandler extends TransferHandler {

    private final FileProcessor fileProcessor;
    private final UIManager uiManager;

    public FileDropHandler(UIManager uiManager, FileProcessor fileProcessor) {
        this.fileProcessor = fileProcessor;
        this.uiManager = uiManager;
        initializeDropTarget();
    }

    private void initializeDropTarget() {
        new DropTarget(uiManager.getLeftPanel(), new DropTargetAdapter() {
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
            uiManager.updatePreviewImages();
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

    private void showErrorDialog(Exception e) {
        JOptionPane.showMessageDialog(uiManager.getLeftPanel(), "Error processing file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
