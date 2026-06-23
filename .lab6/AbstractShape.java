package shapes;

import java.awt.Graphics;

/**
 * Abstract base class for all geometric shapes
 */
public abstract class AbstractShape {
    protected int x;
    protected int y;

    public AbstractShape(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Abstract method for drawing the shape
     */
    public abstract void draw(Graphics g);

    /**
     * Common method for all shapes - move by delta
     */
    public void move(int deltaX, int deltaY) {
        this.x += deltaX;
        this.y += deltaY;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " at (" + x + ", " + y + ")";
    }
}