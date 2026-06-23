package shapes;

import javax.swing.*;

/**
 * Factory for creating Ellipse objects through dialog input
 */
public class EllipseFactory implements ShapeFactory {

    @Override
    public AbstractShape createShape() {
        JTextField xField = new JTextField(5);
        JTextField yField = new JTextField(5);
        JTextField widthField = new JTextField(5);
        JTextField heightField = new JTextField(5);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Enter Ellipse parameters:"));
        panel.add(new JLabel("X:"));
        panel.add(xField);
        panel.add(new JLabel("Y:"));
        panel.add(yField);
        panel.add(new JLabel("Width:"));
        panel.add(widthField);
        panel.add(new JLabel("Height:"));
        panel.add(heightField);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Create Ellipse", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int x = Integer.parseInt(xField.getText());
                int y = Integer.parseInt(yField.getText());
                int width = Integer.parseInt(widthField.getText());
                int height = Integer.parseInt(heightField.getText());
                return new Ellipse(x, y, width, height);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Invalid input! Please enter integers.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return null;
    }

    @Override
    public String getShapeName() {
        return "Ellipse";
    }
}