package shapes;

import java.awt.Graphics;

/**
 * Shape class for a triangle.
 */
public class Triangle extends AbstractShape {
    private int x2;
    private int y2;
    private int x3;
    private int y3;

    public Triangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        super(x1, y1);
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
    }

    @Override
    public void draw(Graphics g) {
        int[] xPoints = {x, x2, x3};
        int[] yPoints = {y, y2, y3};
        g.drawPolygon(xPoints, yPoints, 3);
        System.out.println("Triangle(" + x + ", " + y + ", " + x2 + ", " + y2 + ", " + x3 + ", " + y3 + ")");
    }

    /**
     * @return second vertex x coordinate.
     */
    public int getX2() {
        return x2;
    }

    /**
     * @return second vertex y coordinate.
     */
    public int getY2() {
        return y2;
    }

    /**
     * @return third vertex x coordinate.
     */
    public int getX3() {
        return x3;
    }

    /**
     * @return third vertex y coordinate.
     */
    public int getY3() {
        return y3;
    }
}