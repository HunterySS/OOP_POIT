package shapes;

import java.awt.Graphics;

/**
 * Shape class for a square (derived from rectangle).
 */
public class Square extends MyRectangle {

    public Square(int x, int y, int side) {
        super(x, y, side, side);
    }

    @Override
    public void draw(Graphics g) {
        g.drawRect(x, y, width, height);
        System.out.println("Square(" + x + ", " + y + ", side=" + width + ")");
    }
}