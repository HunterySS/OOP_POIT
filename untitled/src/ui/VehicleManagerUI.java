package ui;

import model.*;
import storage.VehicleStorage;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * GUI for managing vehicles with CRUD operations and JSON serialization.
 * Modern stylish interface with professional file dialogs.
 */
public class VehicleManagerUI extends JFrame {
    private List<Vehicle> vehicles;
    private VehicleTableModel tableModel;
    private JTable table;
    private JLabel statusLabel;
    private File currentFile = null;

    public VehicleManagerUI() {
        vehicles = new ArrayList<>();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Vehicle Manager - JSON Serialization");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(240, 242, 245));

        // Header panel
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        // Table panel
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);

        // Button panel
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // Status bar
        add(createStatusBar(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel(" VEHICLE MANAGEMENT SYSTEM");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("JSON Serialization Demo");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(189, 195, 199));

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(subtitleLabel, BorderLayout.SOUTH);

        headerPanel.add(textPanel, BorderLayout.WEST);

        // Stats panel
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        statsPanel.setOpaque(false);

        JLabel statsLabel = new JLabel("Total Vehicles: 0");
        statsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statsLabel.setForeground(Color.WHITE);
        statsLabel.setName("statsLabel");

        statsPanel.add(statsLabel);
        headerPanel.add(statsPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Create table
        tableModel = new VehicleTableModel();
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));

        // Center align specific columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(300);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Row 1 - CRUD Operations
        JButton addBtn = createModernButton(" Add Vehicle", new Color(46, 204, 113));
        JButton editBtn = createModernButton(" Edit Vehicle", new Color(52, 152, 219));
        JButton deleteBtn = createModernButton(" Delete Vehicle", new Color(231, 76, 60));
        JButton clearBtn = createModernButton(" Clear All", new Color(149, 165, 166));

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.25;
        buttonPanel.add(addBtn, gbc);
        gbc.gridx = 1;
        buttonPanel.add(editBtn, gbc);
        gbc.gridx = 2;
        buttonPanel.add(deleteBtn, gbc);
        gbc.gridx = 3;
        buttonPanel.add(clearBtn, gbc);

        // Row 2 - File Operations
        JButton saveBtn = createModernButton(" Save", new Color(46, 204, 113));
        JButton saveAsBtn = createModernButton(" Save As...", new Color(52, 152, 219));
        JButton loadBtn = createModernButton(" Load", new Color(52, 152, 219));
        JButton exitBtn = createModernButton(" Exit", new Color(231, 76, 60));

        gbc.gridy = 1;
        gbc.gridx = 0;
        buttonPanel.add(saveBtn, gbc);
        gbc.gridx = 1;
        buttonPanel.add(saveAsBtn, gbc);
        gbc.gridx = 2;
        buttonPanel.add(loadBtn, gbc);
        gbc.gridx = 3;
        buttonPanel.add(exitBtn, gbc);

        // Add action listeners
        addBtn.addActionListener(e -> addVehicle());
        editBtn.addActionListener(e -> editVehicle());
        deleteBtn.addActionListener(e -> deleteVehicle());
        clearBtn.addActionListener(e -> clearAll());
        saveBtn.addActionListener(e -> saveToFile(false));
        saveAsBtn.addActionListener(e -> saveToFile(true));
        loadBtn.addActionListener(e -> loadFromFile());
        exitBtn.addActionListener(e -> exitApplication());

        return buttonPanel;
    }

    private JButton createModernButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });

        return button;
    }

    private JPanel createStatusBar() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(new Color(52, 73, 94));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        statusLabel = new JLabel("● Ready | No file loaded | 0 vehicles");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusLabel.setForeground(new Color(189, 195, 199));

        statusPanel.add(statusLabel, BorderLayout.WEST);

        return statusPanel;
    }

    private void updateStatus(String message) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        statusLabel.setText("● " + time + " | " + message + " | " + vehicles.size() + " vehicles");

        // Update header stats
        Component headerPanel = ((JPanel)getContentPane().getComponent(0)).getComponent(0);
        if (headerPanel instanceof JPanel) {
            JPanel statsPanel = (JPanel) ((JPanel) headerPanel).getComponent(1);
            JLabel statsLabel = (JLabel) statsPanel.getComponent(0);
            statsLabel.setText("Total Vehicles: " + vehicles.size());
        }
    }

    private void addVehicle() {
        Vehicle newVehicle = showVehicleDialog(null);
        if (newVehicle != null) {
            vehicles.add(newVehicle);
            tableModel.fireTableDataChanged();
            updateStatus("Added: " + newVehicle.getBrand() + " " + newVehicle.getClass().getSimpleName());
            showNotification("✅ Vehicle added successfully!", new Color(46, 204, 113));
        }
    }

    private void editVehicle() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            Vehicle selected = vehicles.get(selectedRow);
            Vehicle edited = showVehicleDialog(selected);
            if (edited != null) {
                vehicles.set(selectedRow, edited);
                tableModel.fireTableDataChanged();
                updateStatus("Edited: " + edited.getBrand() + " " + edited.getClass().getSimpleName());
                showNotification("✏️ Vehicle updated!", new Color(52, 152, 219));
            }
        } else {
            showWarning("Please select a vehicle to edit");
        }
    }

    private void deleteVehicle() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            Vehicle toDelete = vehicles.get(selectedRow);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Delete " + toDelete.getClass().getSimpleName() + "?\nBrand: " + toDelete.getBrand(),
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                vehicles.remove(selectedRow);
                tableModel.fireTableDataChanged();
                updateStatus("Deleted vehicle");
                showNotification("🗑️ Vehicle deleted!", new Color(231, 76, 60));
            }
        } else {
            showWarning("Please select a vehicle to delete");
        }
    }

    private void clearAll() {
        if (vehicles.isEmpty()) {
            showWarning("List is already empty");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete ALL " + vehicles.size() + " vehicles?",
                "Clear All",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            vehicles.clear();
            tableModel.fireTableDataChanged();
            currentFile = null;
            updateStatus("Cleared all vehicles");
            showNotification("🧹 All vehicles cleared!", new Color(149, 165, 166));
        }
    }

    private void saveToFile(boolean saveAs) {
        if (vehicles.isEmpty()) {
            showWarning("No vehicles to save");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Vehicles");
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON Files (*.json)", "json"));

        if (saveAs || currentFile == null) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            fileChooser.setSelectedFile(new File("vehicles_" + timestamp + ".json"));

            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().endsWith(".json")) {
                    file = new File(file.getAbsolutePath() + ".json");
                }
                saveFile(file);
            }
        } else {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Save to " + currentFile.getName() + "?",
                    "Save",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                saveFile(currentFile);
            }
        }
    }

    private void saveFile(File file) {
        try {
            VehicleStorage.saveToFile(vehicles, file);
            currentFile = file;
            updateStatus("Saved: " + file.getName());
            JOptionPane.showMessageDialog(this,
                    "✅ Successfully saved " + vehicles.size() + " vehicles!\n" + file.getName(),
                    "Save Successful",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            showError("Save Failed", ex.getMessage());
        }
    }

    private void loadFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Vehicles");
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON Files (*.json)", "json"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                List<Vehicle> loaded = VehicleStorage.loadFromFile(file);
                vehicles = loaded;
                currentFile = file;
                tableModel.fireTableDataChanged();
                updateStatus("Loaded: " + file.getName());
                JOptionPane.showMessageDialog(this,
                        "✅ Loaded " + vehicles.size() + " vehicles from:\n" + file.getName(),
                        "Load Successful",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                showError("Load Failed", ex.getMessage());
            }
        }
    }

    private void exitApplication() {
        if (!vehicles.isEmpty() && currentFile == null) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Save before exiting?",
                    "Unsaved Changes",
                    JOptionPane.YES_NO_CANCEL_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                saveToFile(true);
                System.exit(0);
            } else if (confirm == JOptionPane.NO_OPTION) {
                System.exit(0);
            }
        } else {
            System.exit(0);
        }
    }

    private Vehicle showVehicleDialog(Vehicle existing) {
        String[] types = {"Car", "Truck", "Motorcycle", "Bus", "ElectricCar"};
        String type;

        if (existing == null) {
            type = (String) JOptionPane.showInputDialog(this,
                    "Select vehicle type:", "Add Vehicle",
                    JOptionPane.PLAIN_MESSAGE, null, types, types[0]);
            if (type == null) return null;
        } else {
            type = existing.getClass().getSimpleName();
        }

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField brandField = new JTextField(existing != null ? existing.getBrand() : "", 15);
        JTextField yearField = new JTextField(existing != null ? String.valueOf(existing.getYear()) : "", 15);
        JTextField priceField = new JTextField(existing != null ? String.valueOf(existing.getPrice()) : "", 15);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Brand:"), gbc);
        gbc.gridx = 1;
        panel.add(brandField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Year:"), gbc);
        gbc.gridx = 1;
        panel.add(yearField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Price ($):"), gbc);
        gbc.gridx = 1;
        panel.add(priceField, gbc);

        JTextField field1 = new JTextField(15);
        JTextField field2 = new JTextField(15);

        switch (type) {
            case "Car":
                gbc.gridx = 0; gbc.gridy = 3;
                panel.add(new JLabel("Doors:"), gbc);
                gbc.gridx = 1;
                panel.add(field1, gbc);
                if (existing instanceof Car) field1.setText(String.valueOf(((Car) existing).getDoors()));
                break;
            case "ElectricCar":
                gbc.gridx = 0; gbc.gridy = 3;
                panel.add(new JLabel("Doors:"), gbc);
                gbc.gridx = 1;
                panel.add(field1, gbc);
                gbc.gridx = 0; gbc.gridy = 4;
                panel.add(new JLabel("Battery Range (km):"), gbc);
                gbc.gridx = 1;
                panel.add(field2, gbc);
                if (existing instanceof ElectricCar) {
                    field1.setText(String.valueOf(((ElectricCar) existing).getDoors()));
                    field2.setText(String.valueOf(((ElectricCar) existing).getBatteryRange()));
                }
                break;
            case "Truck":
                gbc.gridx = 0; gbc.gridy = 3;
                panel.add(new JLabel("Load Capacity (tons):"), gbc);
                gbc.gridx = 1;
                panel.add(field1, gbc);
                if (existing instanceof Truck) field1.setText(String.valueOf(((Truck) existing).getLoadCapacity()));
                break;
            case "Motorcycle":
                JCheckBox sidecarBox = new JCheckBox("Has Sidecar");
                if (existing instanceof Motorcycle) sidecarBox.setSelected(((Motorcycle) existing).isHasSidecar());
                gbc.gridx = 0; gbc.gridy = 3;
                panel.add(new JLabel("Sidecar:"), gbc);
                gbc.gridx = 1;
                panel.add(sidecarBox, gbc);
                break;
            case "Bus":
                gbc.gridx = 0; gbc.gridy = 3;
                panel.add(new JLabel("Passenger Capacity:"), gbc);
                gbc.gridx = 1;
                panel.add(field1, gbc);
                if (existing instanceof Bus) field1.setText(String.valueOf(((Bus) existing).getPassengerCapacity()));
                break;
        }

        int result = JOptionPane.showConfirmDialog(this, panel,
                existing == null ? "Add " + type : "Edit " + type,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String brand = brandField.getText().trim();
                if (brand.isEmpty()) throw new Exception("Brand required");
                int year = Integer.parseInt(yearField.getText().trim());
                double price = Double.parseDouble(priceField.getText().trim());

                switch (type) {
                    case "Car":
                        return new Car(brand, year, price, Integer.parseInt(field1.getText().trim()));
                    case "ElectricCar":
                        return new ElectricCar(brand, year, price,
                                Integer.parseInt(field1.getText().trim()),
                                Integer.parseInt(field2.getText().trim()));
                    case "Truck":
                        return new Truck(brand, year, price, Double.parseDouble(field1.getText().trim()));
                    case "Motorcycle":
                        JCheckBox box = (JCheckBox) panel.getComponent(panel.getComponentCount() - 1);
                        return new Motorcycle(brand, year, price, box.isSelected());
                    case "Bus":
                        return new Bus(brand, year, price, Integer.parseInt(field1.getText().trim()));
                }
            } catch (Exception e) {
                showError("Invalid Input", e.getMessage());
            }
        }
        return null;
    }

    private void showNotification(String message, Color color) {
        JLabel notification = new JLabel(message);
        notification.setOpaque(true);
        notification.setBackground(color);
        notification.setForeground(Color.WHITE);
        notification.setFont(new Font("Segoe UI", Font.BOLD, 13));
        notification.setHorizontalAlignment(SwingConstants.CENTER);
        notification.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        add(notification, BorderLayout.NORTH);
        revalidate();

        Timer timer = new Timer(2000, e -> {
            remove(notification);
            revalidate();
            repaint();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    private void showError(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    class VehicleTableModel extends AbstractTableModel {
        private final String[] columns = {"Type", "Brand", "Year", "Price ($)", "Details"};

        @Override
        public int getRowCount() { return vehicles.size(); }
        @Override
        public int getColumnCount() { return columns.length; }
        @Override
        public String getColumnName(int col) { return columns[col]; }

        @Override
        public Object getValueAt(int row, int col) {
            Vehicle v = vehicles.get(row);
            switch (col) {
                case 0: return v.getClass().getSimpleName();
                case 1: return v.getBrand();
                case 2: return v.getYear();
                case 3: return String.format("%.2f", v.getPrice());
                case 4:
                    if (v instanceof Car && !(v instanceof ElectricCar))
                        return "Doors: " + ((Car) v).getDoors();
                    if (v instanceof ElectricCar)
                        return "Doors: " + ((ElectricCar) v).getDoors() + ", Range: " + ((ElectricCar) v).getBatteryRange() + " km";
                    if (v instanceof Truck)
                        return "Capacity: " + ((Truck) v).getLoadCapacity() + " tons";
                    if (v instanceof Motorcycle)
                        return "Sidecar: " + (((Motorcycle) v).isHasSidecar() ? "Yes" : "No");
                    if (v instanceof Bus)
                        return "Passengers: " + ((Bus) v).getPassengerCapacity();
                    return "";
                default: return "";
            }
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new VehicleManagerUI().setVisible(true);
        });
    }
}