package shapes;

import javax.swing.*;

/**
 * Factory for creating Square objects through dialog input
 */
public class SquareFactory implements ShapeFactory {

    @Override
    public AbstractShape createShape() {
        JTextField xField = new JTextField(5);
        JTextField yField = new JTextField(5);
        JTextField sideField = new JTextField(5);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Enter Square parameters:"));
        panel.add(new JLabel("X:"));
        panel.add(xField);
        panel.add(new JLabel("Y:"));
        panel.add(yField);
        panel.add(new JLabel("Side:"));
        panel.add(sideField);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Create Square", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int x = Integer.parseInt(xField.getText());
                int y = Integer.parseInt(yField.getText());
                int side = Integer.parseInt(sideField.getText());
                return new Square(x, y, side);
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
        return "Square";
    }
}