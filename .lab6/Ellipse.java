package shapes;

import java.awt.Graphics;

/**
 * Shape class for an ellipse.
 */
public class Ellipse extends AbstractShape {
    protected int width;
    protected int height;

    public Ellipse(int x, int y, int width, int height) {
        super(x, y);
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Graphics g) {
        g.drawOval(x, y, width, height);
        System.out.println("Ellipse(" + x + ", " + y + ", " + width + ", " + height + ")");
    }

    /**
     * @return ellipse width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return ellipse height.
     */
    public int getHeight() {
        return height;
    }
}