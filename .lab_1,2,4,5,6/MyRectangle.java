package shapes;

import java.awt.Graphics;

/**
 * Shape class for a rectangle.
 */
public class MyRectangle extends AbstractShape {
    protected int width;
    protected int height;

    public MyRectangle(int x, int y, int width, int height) {
        super(x, y);
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Graphics g) {
        g.drawRect(x, y, width, height);
        System.out.println("Rectangle(" + x + ", " + y + ", " + width + ", " + height + ")");
    }

    /**
     * @return rectangle width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return rectangle height.
     */
    public int getHeight() {
        return height;
    }
}