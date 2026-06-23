package shapes;

import javax.swing.*;

/**
 * Factory for creating Line objects through dialog input
 */
public class LineFactory implements ShapeFactory {

    @Override
    public AbstractShape createShape() {
        JTextField x1Field = new JTextField(5);
        JTextField y1Field = new JTextField(5);
        JTextField x2Field = new JTextField(5);
        JTextField y2Field = new JTextField(5);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Enter Line parameters:"));
        panel.add(new JLabel("Start X:"));
        panel.add(x1Field);
        panel.add(new JLabel("Start Y:"));
        panel.add(y1Field);
        panel.add(new JLabel("End X:"));
        panel.add(x2Field);
        panel.add(new JLabel("End Y:"));
        panel.add(y2Field);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Create Line", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int x1 = Integer.parseInt(x1Field.getText());
                int y1 = Integer.parseInt(y1Field.getText());
                int x2 = Integer.parseInt(x2Field.getText());
                int y2 = Integer.parseInt(y2Field.getText());
                return new Line(x1, y1, x2, y2);
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
        return "Line";
    }
}