package io;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import shapes.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Serializes and deserializes shape model into XML.
 */
public final class ShapeXmlSerializer {
    private ShapeXmlSerializer() {
    }

    /**
     * Converts in-memory shape list to canonical XML format.
     */
    public static String toXml(List<AbstractShape> shapes) {
        StringBuilder builder = new StringBuilder();
        builder.append("<shapes>\n");
        for (AbstractShape shape : shapes) {
            builder.append("  ").append(shapeToXml(shape)).append("\n");
        }
        builder.append("</shapes>\n");
        return builder.toString();
    }

    /**
     * Restores known shape classes from XML content.
     */
    public static List<AbstractShape> fromXml(String xmlContent) {
        List<AbstractShape> shapes = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlContent)));
            NodeList nodes = document.getElementsByTagName("shape");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element shapeElement = (Element) nodes.item(i);
                AbstractShape shape = xmlToShape(shapeElement);
                if (shape != null) {
                    shapes.add(shape);
                }
            }
        } catch (Exception exception) {
            throw new IllegalArgumentException("Invalid XML shape format.", exception);
        }
        return shapes;
    }

    /**
     * Produces pretty XML to improve readability in saved files.
     */
    public static String prettyFormat(String xmlContent) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlContent)));

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));
            return writer.toString();
        } catch (Exception exception) {
            return xmlContent;
        }
    }

    private static String shapeToXml(AbstractShape shape) {
        if (shape instanceof Line line) {
            return "<shape type=\"Line\" x1=\"" + line.getX() + "\" y1=\"" + line.getY() +
                    "\" x2=\"" + line.getX2() + "\" y2=\"" + line.getY2() + "\"/>";
        }
        if (shape instanceof Triangle triangle) {
            return "<shape type=\"Triangle\" x1=\"" + triangle.getX() + "\" y1=\"" + triangle.getY() +
                    "\" x2=\"" + triangle.getX2() + "\" y2=\"" + triangle.getY2() +
                    "\" x3=\"" + triangle.getX3() + "\" y3=\"" + triangle.getY3() + "\"/>";
        }
        if (shape instanceof Circle circle) {
            return "<shape type=\"Circle\" x=\"" + circle.getX() + "\" y=\"" + circle.getY() +
                    "\" radius=\"" + (circle.getWidth() / 2) + "\"/>";
        }
        if (shape instanceof Square square) {
            return "<shape type=\"Square\" x=\"" + square.getX() + "\" y=\"" + square.getY() +
                    "\" side=\"" + square.getWidth() + "\"/>";
        }
        if (shape instanceof Ellipse ellipse) {
            return "<shape type=\"Ellipse\" x=\"" + ellipse.getX() + "\" y=\"" + ellipse.getY() +
                    "\" width=\"" + ellipse.getWidth() + "\" height=\"" + ellipse.getHeight() + "\"/>";
        }
        if (shape instanceof MyRectangle rectangle) {
            return "<shape type=\"Rectangle\" x=\"" + rectangle.getX() + "\" y=\"" + rectangle.getY() +
                    "\" width=\"" + rectangle.getWidth() + "\" height=\"" + rectangle.getHeight() + "\"/>";
        }
        return "<shape type=\"Unknown\"/>";
    }

    private static AbstractShape xmlToShape(Element element) {
        String type = element.getAttribute("type");
        try {
            return switch (type) {
                case "Line" -> new Line(
                        intAttr(element, "x1"),
                        intAttr(element, "y1"),
                        intAttr(element, "x2"),
                        intAttr(element, "y2")
                );
                case "Rectangle" -> new MyRectangle(
                        intAttr(element, "x"),
                        intAttr(element, "y"),
                        intAttr(element, "width"),
                        intAttr(element, "height")
                );
                case "Ellipse" -> new Ellipse(
                        intAttr(element, "x"),
                        intAttr(element, "y"),
                        intAttr(element, "width"),
                        intAttr(element, "height")
                );
                case "Circle" -> new Circle(
                        intAttr(element, "x"),
                        intAttr(element, "y"),
                        intAttr(element, "radius")
                );
                case "Square" -> new Square(
                        intAttr(element, "x"),
                        intAttr(element, "y"),
                        intAttr(element, "side")
                );
                case "Triangle" -> new Triangle(
                        intAttr(element, "x1"),
                        intAttr(element, "y1"),
                        intAttr(element, "x2"),
                        intAttr(element, "y2"),
                        intAttr(element, "x3"),
                        intAttr(element, "y3")
                );
                default -> null;
            };
        } catch (Exception exception) {
            return null;
        }
    }

    private static int intAttr(Element element, String attributeName) {
        return Integer.parseInt(element.getAttribute(attributeName));
    }
}
