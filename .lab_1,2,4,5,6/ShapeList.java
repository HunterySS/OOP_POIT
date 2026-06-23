package shapes;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class that stores and manages all shapes
 * Part of the MVC pattern - this is the Model
 */
public class ShapeList {
    private List<AbstractShape> shapes;

    public ShapeList() {
        shapes = new ArrayList<>();
    }

    /**
     * Adds a shape to the list
     * @param shape The shape to add
     */
    public void addShape(AbstractShape shape) {
        if (shape != null) {
            shapes.add(shape);
        }
    }

    /**
     * Removes the last added shape
     */
    public void removeLastShape() {
        if (!shapes.isEmpty()) {
            shapes.remove(shapes.size() - 1);
        }
    }

    public void clear() {
        shapes.clear();
    }

    public void drawAll(Graphics g) {
        for (AbstractShape shape : shapes) {
            shape.draw(g);
        }
    }


    public int size() {
        return shapes.size();
    }

    /**
     * Returns a defensive copy of all shapes for serialization.
     */
    public List<AbstractShape> getShapesSnapshot() {
        return new ArrayList<>(shapes);
    }

    /**
     * Replaces current list with loaded data from file.
     */
    public void replaceAll(List<AbstractShape> loadedShapes) {
        shapes.clear();
        if (loadedShapes != null) {
            shapes.addAll(loadedShapes);
        }
    }

    /**
     * @return String representation of all shapes (for debugging)
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ShapeList contains ").append(shapes.size()).append(" shapes:\n");
        for (int i = 0; i < shapes.size(); i++) {
            sb.append("  [").append(i).append("] ").append(shapes.get(i).getClass().getSimpleName());
            sb.append(" at (").append(shapes.get(i).getX()).append(", ").append(shapes.get(i).getY()).append(")\n");
        }
        return sb.toString();
    }
}