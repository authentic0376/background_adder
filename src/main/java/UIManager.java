import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class UIManager {

    private JLabel processedPreview;
    private FileProcessor fileProcessor;
    private JFrame frame;
    private JPanel leftPanel;
    private JLabel originalPreview;
    private JPanel rightPanel;
    private JPanel checkerboard;
    private JButton saveButton;
    private JSplitPane splitPane;
    private Map<String, UpdatePreviewStrategy> updatePreviewHandlers;

    public void initialize() {
        initComponents();
        assembleComponents();

        Map<String, FileHandlerStrategy> fileHandlers = new HashMap<>();
        fileHandlers.put("png", new PngHandler());
        fileHandlers.put("svg", new SvgHandler());

        fileProcessor = new FileProcessor(fileHandlers);

        saveButton.addActionListener(e -> fileProcessor.saveProcessedImage(processedPreview));
        new FileDropHandler(this, fileProcessor);


        updatePreviewHandlers = new HashMap<>();
        updatePreviewHandlers.put("png", new PngPreviewHandler());
        updatePreviewHandlers.put("svg", new SvgPreviewHandler());

        finalFrameSetting();
    }

    private void initComponents() {
        initFrame();
        initLeftPanel();
        initOriginalPreview();
        initCheckBoard();
        initRightPanel();
        initProcessedPreview();
        initSaveButton();
        initSplitPane();
    }

    public void updatePreviewImages() {
        UpdatePreviewStrategy strategy = updatePreviewHandlers.get(fileProcessor.getExtension());
        strategy.updatePreviewImage(fileProcessor.getOriginalImage(), originalPreview);
        strategy.updatePreviewImage(fileProcessor.getProcessedImage(), processedPreview);
    }

    private void initSaveButton() {
        saveButton = new JButton("Execute");
    }

    private void initRightPanel() {
        rightPanel = createPreviewPanel("Preview Image");
    }

    private void initLeftPanel() {
        leftPanel = createPreviewPanel("Drag and drop your files here");
    }

    private void initSplitPane() {
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5); // Equal panel sizes
    }

    private void initProcessedPreview() {
        processedPreview = new JLabel("Processed Image Preview", JLabel.CENTER);
        processedPreview.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void initCheckBoard() {
        checkerboard = createCheckerboardPanel();
        checkerboard.setLayout(new BorderLayout());
    }

    private void initOriginalPreview() {
        originalPreview = new JLabel("Original Image Preview", JLabel.CENTER);
        originalPreview.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void assembleComponents() {
        checkerboard.add(originalPreview, BorderLayout.CENTER);
        leftPanel.add(checkerboard, BorderLayout.CENTER);

        rightPanel.add(processedPreview, BorderLayout.CENTER);
        rightPanel.add(saveButton, BorderLayout.SOUTH);

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);

        frame.add(splitPane, BorderLayout.CENTER);
    }

    private void finalFrameSetting() {
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void initFrame() {
        frame = new JFrame("Image Background Adder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        setIcon();
    }

    private void setIcon() {
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(UIManager.class.getResource("background adder.png")));
        Image img = icon.getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH);
        frame.setIconImage(img);
    }

    private JPanel createPreviewPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setBackground(Color.LIGHT_GRAY);
        return panel;
    }

    private JPanel createCheckerboardPanel() {
        JPanel jPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int tileSize = 10;
                for (int y = 0; y < getHeight(); y += tileSize) {
                    for (int x = 0; x < getWidth(); x += tileSize) {
                        boolean isLightTile = (x / tileSize + y / tileSize) % 2 == 0;
                        g.setColor(isLightTile ? Color.WHITE : Color.GRAY);
                        g.fillRect(x, y, tileSize, tileSize);
                    }
                }
            }
        };
        return jPanel;
    }

    public JPanel getLeftPanel() {
        return leftPanel;
    }

}