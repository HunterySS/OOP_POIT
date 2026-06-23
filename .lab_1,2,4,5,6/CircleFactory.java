package shapes;

import javax.swing.*;

/**
 * Factory for creating Circle objects through dialog input
 */
public class CircleFactory implements ShapeFactory {

    @Override
    public AbstractShape createShape() {
        JTextField xField = new JTextField(5);
        JTextField yField = new JTextField(5);
        JTextField radiusField = new JTextField(5);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Enter Circle parameters:"));
        panel.add(new JLabel("Center X:"));
        panel.add(xField);
        panel.add(new JLabel("Center Y:"));
        panel.add(yField);
        panel.add(new JLabel("Radius:"));
        panel.add(radiusField);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Create Circle", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int x = Integer.parseInt(xField.getText());
                int y = Integer.parseInt(yField.getText());
                int radius = Integer.parseInt(radiusField.getText());
                return new Circle(x, y, radius);
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
        return "Circle";
    }
}