package starplugin;

import shapes.AbstractShape;
import shapes.ShapeFactory;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Factory that creates Star shapes from dialog input.
 */
public class StarFactory implements ShapeFactory {
    @Override
    public AbstractShape createShape() {
        JTextField xField = new JTextField(6);
        JTextField yField = new JTextField(6);
        JTextField radiusField = new JTextField(6);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Enter Star parameters:"));
        panel.add(new JLabel("Center X:"));
        panel.add(xField);
        panel.add(new JLabel("Center Y:"));
        panel.add(yField);
        panel.add(new JLabel("Outer radius:"));
        panel.add(radiusField);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Create Star",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        try {
            int x = Integer.parseInt(xField.getText());
            int y = Integer.parseInt(yField.getText());
            int radius = Integer.parseInt(radiusField.getText());
            if (radius <= 0) {
                throw new NumberFormatException("Radius must be positive");
            }
            return new Star(x, y, radius);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Invalid numeric input for Star.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return null;
        }
    }

    @Override
    public String getShapeName() {
        return "Star (plugin)";
    }
}
