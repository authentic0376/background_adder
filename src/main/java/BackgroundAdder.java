import javax.swing.*;

public class BackgroundAdder {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UIManager uiManager = new UIManager();
            uiManager.initialize();
        });
    }
}