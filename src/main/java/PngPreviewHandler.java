import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class PngPreviewHandler implements UpdatePreviewStrategy {
    @Override
    public void updatePreviewImages(FileProcessor fileProcessor, UIManager uiManager) {
        updatePreviewImage(fileProcessor.getOriginalImage(), uiManager.getOriginalPreview());
        updatePreviewImage(fileProcessor.getProcessedImage(), uiManager.getProcessedPreview());
    }

    private void updatePreviewImage(BufferedImage image, JLabel preview) {
        ImageIcon originalIcon = scaleImageToLabel(image, preview);
        preview.setIcon(originalIcon);
        preview.setText(null);
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
}
