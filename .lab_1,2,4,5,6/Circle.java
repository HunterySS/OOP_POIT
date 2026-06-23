package shapes;

import java.awt.Graphics;

/**
 * Shape class for a circle (derived from ellipse).
 */
public class Circle extends Ellipse {

    public Circle(int x, int y, int radius) {
        super(x, y, radius * 2, radius * 2);
    }

    @Override
    public void draw(Graphics g) {
        g.drawOval(x, y, width, height);
        System.out.println("Circle(center at " + (x + width/2) + ", " + (y + height/2) +
                ", radius=" + (width/2) + ")");
    }
}