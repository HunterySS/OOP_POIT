package starplugin;

import shapes.AbstractShape;

import java.awt.Graphics;
import java.awt.Polygon;

/**
 * Plugin shape representing a simple five-point star.
 */
public class Star extends AbstractShape {
    private final int outerRadius;

    public Star(int centerX, int centerY, int outerRadius) {
        super(centerX, centerY);
        this.outerRadius = outerRadius;
    }

    @Override
    public void draw(Graphics g) {
        int innerRadius = Math.max(3, outerRadius / 2);
        Polygon polygon = new Polygon();

        for (int i = 0; i < 10; i++) {
            double angle = Math.toRadians(-90 + i * 36);
            int radius = (i % 2 == 0) ? outerRadius : innerRadius;
            int px = x + (int) (radius * Math.cos(angle));
            int py = y + (int) (radius * Math.sin(angle));
            polygon.addPoint(px, py);
        }
        g.drawPolygon(polygon);
    }
}
