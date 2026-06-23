package shapes;

/**
 * Factory interface for creating shapes without coupling to concrete classes.
 * This is the key to the Open/Closed Principle - new shapes can be added
 * by creating new factories without modifying existing code.
 */
public interface ShapeFactory {
    /**
     * Creates a shape based on user input through dialog boxes
     * @return A new Shape object or null if creation was cancelled
     */
    AbstractShape createShape();
    String getShapeName();
}