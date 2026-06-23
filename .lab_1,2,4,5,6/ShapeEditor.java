import io.ShapeXmlSerializer;
import processing.DataProcessingPlugin;
import processing.ProcessingPipeline;
import shapes.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ShapeEditor extends JFrame {
    private static final String DEFAULT_PLUGIN_DIR = "plugins";
    private static final int BUILT_IN_FACTORY_COUNT = 6;

    private final ShapeList shapeList;
    private final DrawingPanel drawingPanel;
    private final JLabel statusLabel;
    private final JLabel pluginLabel;
    /** Displays the names of manually loaded jar files in the top toolbar. */
    private final JLabel loadedPluginsLabel;
    private final JComboBox<String> shapeTypeCombo;
    private final List<ShapeFactory> factories;
    private final ProcessingPipeline processingPipeline;

    /**
     * Builds editor window and initializes built-in and plugin factories.
     */
    public ShapeEditor(String[] args) {
        setTitle("Shape editor with plugin hierarchy and processing plugins");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        shapeList = new ShapeList();
        factories = new ArrayList<>();
        processingPipeline = new ProcessingPipeline();
        registerBuiltInFactories();
        registerPluginFactories(args);
        registerProcessingPlugins();

        drawingPanel = new DrawingPanel();
        statusLabel = new JLabel();
        pluginLabel = new JLabel();
        loadedPluginsLabel = new JLabel();
        shapeTypeCombo = new JComboBox<>();

        initializeUI();
        addDemoShapes();
        refreshFactoryCombo();
        updateStatus();
        updatePluginStatus();
    }

    /**
     * Registers built-in factories that are always available.
     */
    private void registerBuiltInFactories() {
        factories.add(new LineFactory());
        factories.add(new MyRectangleFactory());
        factories.add(new EllipseFactory());
        factories.add(new CircleFactory());
        factories.add(new SquareFactory());
        factories.add(new TriangleFactory());
    }

    /**
     * Loads plugin factories from folder and optional class names.
     */
    private void registerPluginFactories(String[] args) {
        List<ShapeFactory> pluginFactories = pluginsystem.PluginLoader.loadFactories(DEFAULT_PLUGIN_DIR, args);
        factories.addAll(pluginFactories);
    }

    /**
     * Registers built-in and external processing plugins.
     */
    private void registerProcessingPlugins() {
        List<DataProcessingPlugin> plugins = pluginsystem.ProcessingPluginLoader.loadBuiltIns();
        plugins.addAll(pluginsystem.ProcessingPluginLoader.loadFromFolder(DEFAULT_PLUGIN_DIR));
        for (DataProcessingPlugin plugin : plugins) {
            processingPipeline.addPlugin(plugin);
        }
    }

    /**
     * Adds a few objects to preview drawing capabilities at startup.
     */
    private void addDemoShapes() {
        shapeList.addShape(new Line(100, 100, 200, 100));
        shapeList.addShape(new MyRectangle(250, 80, 120, 80));
        shapeList.addShape(new Ellipse(100, 220, 150, 80));
        shapeList.addShape(new Circle(500, 150, 50));
        shapeList.addShape(new Square(120, 350, 80));
        shapeList.addShape(new Triangle(450, 350, 500, 280, 550, 350));
    }

    /**
     * Creates user interface controls and drawing panel.
     */
    private void initializeUI() {
        setJMenuBar(buildMenuBar());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.add(new JLabel("Shape:"));
        controlPanel.add(shapeTypeCombo);

        JButton createButton = new JButton("Create");
        createButton.addActionListener(e -> createShapeFromSelectedFactory());
        controlPanel.add(createButton);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            shapeList.clear();
            drawingPanel.repaint();
            updateStatus();
        });
        controlPanel.add(clearButton);

        JButton reloadPluginsButton = new JButton("Reload plugins");
        reloadPluginsButton.addActionListener(e -> reloadPlugins());
        controlPanel.add(reloadPluginsButton);

        JButton pluginSettingsButton = new JButton("Plugin settings");
        pluginSettingsButton.addActionListener(e -> showPluginSettingsDialog());
        controlPanel.add(pluginSettingsButton);

        topPanel.add(controlPanel, BorderLayout.WEST);

        // Label in the center shows names of all manually loaded jar files.
        loadedPluginsLabel.setForeground(new Color(0, 60, 160));
        loadedPluginsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        loadedPluginsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(loadedPluginsLabel, BorderLayout.CENTER);

        pluginLabel.setForeground(new Color(0, 90, 0));
        pluginLabel.setFont(new Font("Arial", Font.BOLD, 12));
        topPanel.add(pluginLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        drawingPanel.setBackground(Color.WHITE);
        drawingPanel.setPreferredSize(new Dimension(900, 550));
        drawingPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

        add(new JScrollPane(drawingPanel), BorderLayout.CENTER);

        statusLabel.setText("Ready. Total shapes: " + shapeList.size());
        statusLabel.setBorder(BorderFactory.createEtchedBorder());
        add(statusLabel, BorderLayout.SOUTH);
    }

    /**
     * Rebuilds combo values from the current factory list.
     */
    private void refreshFactoryCombo() {
        shapeTypeCombo.removeAllItems();
        for (ShapeFactory factory : factories) {
            shapeTypeCombo.addItem(factory.getShapeName());
        }
    }

    /**
     * Creates a shape using the currently selected factory.
     */
    private void createShapeFromSelectedFactory() {
        int index = shapeTypeCombo.getSelectedIndex();
        if (index < 0 || index >= factories.size()) {
            return;
        }
        AbstractShape shape = factories.get(index).createShape();
        if (shape != null) {
            shapeList.addShape(shape);
            drawingPanel.repaint();
            updateStatus();
        }
    }

    /**
     * Reloads plugin factories while preserving built-in ones.
     */
    private void reloadPlugins() {
        factories.clear();
        registerBuiltInFactories();
        registerPluginFactories(new String[0]);
        refreshFactoryCombo();
        updatePluginStatus();
    }

    /**
     * Opens save dialog and persists transformed shape data to file.
     */
    private void saveToFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save shapes");
        chooser.setFileFilter(new FileNameExtensionFilter("Data files", "dat", "txt", "json"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            String xmlContent = ShapeXmlSerializer.toXml(shapeList.getShapesSnapshot());
            String prepared = processingPipeline.applyBeforeSave(xmlContent);
            Files.writeString(chooser.getSelectedFile().toPath(), prepared, StandardCharsets.UTF_8);
            statusLabel.setText("Saved: " + chooser.getSelectedFile().getName());
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this,
                    "Save failed: " + exception.getMessage(),
                    "Save error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Loads file, runs reverse plugin pipeline, and restores shape list.
     */
    private void loadFromFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Load shapes");
        chooser.setFileFilter(new FileNameExtensionFilter("Data files", "dat", "txt", "json"));
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            String loadedContent = Files.readString(chooser.getSelectedFile().toPath(), StandardCharsets.UTF_8);
            String xmlContent = processingPipeline.applyAfterLoad(loadedContent);
            shapeList.replaceAll(ShapeXmlSerializer.fromXml(xmlContent));
            drawingPanel.repaint();
            updateStatus();
            statusLabel.setText("Loaded: " + chooser.getSelectedFile().getName());
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this,
                    "Load failed: " + exception.getMessage(),
                    "Load error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Opens settings dialog where plugin availability and parameters are controlled.
     */
    private void showPluginSettingsDialog() {
        JDialog dialog = new JDialog(this, "Plugin settings", true);
        dialog.setLayout(new BorderLayout());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        for (ProcessingPipeline.PluginState state : processingPipeline.getPluginStates()) {
            JPanel pluginPanel = new JPanel(new BorderLayout());
            pluginPanel.setBorder(BorderFactory.createTitledBorder(state.getPlugin().getDisplayName()));

            JCheckBox enabledBox = new JCheckBox("Enabled", state.isEnabled());
            enabledBox.addActionListener(e -> state.setEnabled(enabledBox.isSelected()));
            pluginPanel.add(enabledBox, BorderLayout.NORTH);

            JPanel settingsPanel = state.getPlugin().createSettingsPanel();
            if (settingsPanel != null) {
                pluginPanel.add(settingsPanel, BorderLayout.CENTER);
            }

            content.add(pluginPanel);
        }

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        dialog.add(new JScrollPane(content), BorderLayout.CENTER);
        dialog.add(closeButton, BorderLayout.SOUTH);
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    /**
     * Allows user to pick plugin jar manually and load it at runtime.
     */
    private void loadProcessingPluginFromFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Load processing plugin jar");
        chooser.setFileFilter(new FileNameExtensionFilter("Jar files", "jar"));
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        List<DataProcessingPlugin> loaded = pluginsystem.ProcessingPluginLoader.loadFromJar(
                Path.of(chooser.getSelectedFile().getAbsolutePath())
        );
        for (DataProcessingPlugin plugin : loaded) {
            processingPipeline.addPlugin(plugin);
        }

        JOptionPane.showMessageDialog(this,
                "Loaded processing plugins: " + loaded.size(),
                "Plugin loading",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Allows user to pick any shape plugin jar and load it at runtime without
     * requiring a signature file. This is the intended entry point for loading
     * a teammate's plugin that was built against the same ShapeFactory interface
     * but was never signed with this project's private key.
     */
    private void loadShapePluginFromFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Load shape plugin jar (no signature required)");
        chooser.setFileFilter(new FileNameExtensionFilter("Jar files", "jar"));
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        List<ShapeFactory> loaded = pluginsystem.PluginLoader.loadFactoriesFromJar(
                Path.of(chooser.getSelectedFile().getAbsolutePath())
        );

        if (loaded.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No ShapeFactory implementations found in the selected jar.\n" +
                            "Make sure the jar contains a META-INF/services/shapes.ShapeFactory file.",
                    "Shape plugin loading",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Register loaded factories and refresh the shape type combo box.
        factories.addAll(loaded);
        refreshFactoryCombo();
        updatePluginStatus();

        // Append the jar filename to the toolbar label so the user can see
        // which third-party plugins are currently active.
        String jarName = chooser.getSelectedFile().getName();
        String current = loadedPluginsLabel.getText();
        if (current.isEmpty()) {
            loadedPluginsLabel.setText("Loaded: " + jarName);
        } else {
            loadedPluginsLabel.setText(current + ", " + jarName);
        }

        JOptionPane.showMessageDialog(this,
                "Loaded shape factories: " + loaded.size(),
                "Shape plugin loading",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Updates status bar with the amount of shapes.
     */
    private void updateStatus() {
        statusLabel.setText("Total shapes: " + shapeList.size());
    }

    /**
     * Shows plugin loading state in the top panel.
     */
    private void updatePluginStatus() {
        int pluginCount = Math.max(0, factories.size() - BUILT_IN_FACTORY_COUNT);
        pluginLabel.setText("Factories: " + pluginCount + ", processors: " + processingPipeline.getPluginStates().size());
    }

    /**
     * Creates top menu including file operations and plugin setup.
     */
    private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save with plugins");
        saveItem.addActionListener(e -> saveToFile());
        JMenuItem loadItem = new JMenuItem("Load with plugins");
        loadItem.addActionListener(e -> loadFromFile());
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> dispose());
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu settingsMenu = new JMenu("Settings");
        JMenuItem pluginSettingsItem = new JMenuItem("Plugin settings");
        pluginSettingsItem.addActionListener(e -> showPluginSettingsDialog());
        JMenuItem loadPluginFileItem = new JMenuItem("Load processing plugin from file...");
        loadPluginFileItem.addActionListener(e -> loadProcessingPluginFromFile());
        // Menu item that lets the user pick any third-party shape plugin jar at runtime.
        // No signature is required — useful when a teammate shares their plugin jar.
        JMenuItem loadShapePluginItem = new JMenuItem("Load shape plugin from file...");
        loadShapePluginItem.addActionListener(e -> loadShapePluginFromFile());
        settingsMenu.add(pluginSettingsItem);
        settingsMenu.add(loadPluginFileItem);
        settingsMenu.add(loadShapePluginItem);

        menuBar.add(fileMenu);
        menuBar.add(settingsMenu);
        return menuBar;
    }

    /**
     * Canvas panel that draws all current shapes and background guides.
     */
    private class DrawingPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            drawGrid(g2d);
            g2d.setColor(Color.BLUE);
            shapeList.drawAll(g2d);
            drawCoordinateAxes(g2d);
        }

        /**
         * Draws background helper grid.
         */
        private void drawGrid(Graphics2D g2d) {
            g2d.setColor(new Color(230, 230, 230));
            g2d.setStroke(new BasicStroke(0.5f));

            for (int x = 0; x < getWidth(); x += 20) {
                g2d.drawLine(x, 0, x, getHeight());
            }
            for (int y = 0; y < getHeight(); y += 20) {
                g2d.drawLine(0, y, getWidth(), y);
            }
        }

        /**
         * Draws central coordinate axes.
         */
        private void drawCoordinateAxes(Graphics2D g2d) {
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.setStroke(new BasicStroke(1));
            g2d.drawLine(0, getHeight()/2, getWidth(), getHeight()/2);
            g2d.drawLine(getWidth()/2, 0, getWidth()/2, getHeight());
        }
    }

    /**
     * Program entry point.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new ShapeEditor(args).setVisible(true);
        });
    }
}