package shapes;

import javax.swing.*;

/**
 * Factory for creating Triangle objects through dialog input
 */
public class TriangleFactory implements ShapeFactory {

    @Override
    public AbstractShape createShape() {
        JTextField x1Field = new JTextField(5);
        JTextField y1Field = new JTextField(5);
        JTextField x2Field = new JTextField(5);
        JTextField y2Field = new JTextField(5);
        JTextField x3Field = new JTextField(5);
        JTextField y3Field = new JTextField(5);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Enter Triangle parameters:"));
        panel.add(new JLabel("Point 1 X:"));
        panel.add(x1Field);
        panel.add(new JLabel("Point 1 Y:"));
        panel.add(y1Field);
        panel.add(new JLabel("Point 2 X:"));
        panel.add(x2Field);
        panel.add(new JLabel("Point 2 Y:"));
        panel.add(y2Field);
        panel.add(new JLabel("Point 3 X:"));
        panel.add(x3Field);
        panel.add(new JLabel("Point 3 Y:"));
        panel.add(y3Field);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Create Triangle", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int x1 = Integer.parseInt(x1Field.getText());
                int y1 = Integer.parseInt(y1Field.getText());
                int x2 = Integer.parseInt(x2Field.getText());
                int y2 = Integer.parseInt(y2Field.getText());
                int x3 = Integer.parseInt(x3Field.getText());
                int y3 = Integer.parseInt(y3Field.getText());
                return new Triangle(x1, y1, x2, y2, x3, y3);
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
        return "Triangle";
    }
}