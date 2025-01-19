import org.apache.batik.swing.JSVGCanvas;

import javax.swing.*;
import java.awt.*;
import java.io.File;

class SvgPreviewHandler implements UpdatePreviewStrategy {

    @Override
    public void updatePreviewImage(Object image, JLabel preview) {
        try {
            JSVGCanvas svgCanvas = new JSVGCanvas();
            File svgFile = (File) image;
            svgCanvas.setURI(svgFile.toURI().toString());
            svgCanvas.setBackground(new Color(0, 0, 0, 0));

            preview.removeAll();
            preview.setIcon(null);
            preview.setLayout(new BorderLayout());
            preview.add(svgCanvas, BorderLayout.CENTER);

            preview.revalidate();
//            preview.repaint();
        } catch (Exception e) {
            // 오류 로깅 및 사용자에게 명확한 메시지 표시
            System.err.println("Error loading SVG: " + e.getMessage());
            JOptionPane.showMessageDialog(preview, "Failed to load SVG: " + e.getMessage(),
                    "SVG Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
