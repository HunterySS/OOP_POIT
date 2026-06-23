package processing.plugins;

import io.ShapeXmlSerializer;
import processing.DataProcessingPlugin;
import shapes.AbstractShape;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Variant 1 plugin: transforms XML payload to JSON before save and back after load.
 */
public class XmlToJsonTransformPlugin implements DataProcessingPlugin {
    private boolean prettyJson = true;

    @Override
    public String getPluginId() {
        return "xml-json-transform";
    }

    @Override
    public String getDisplayName() {
        return "Variant 1: XML <-> JSON transform";
    }

    @Override
    public String processBeforeSave(String content) {
        List<AbstractShape> shapes = ShapeXmlSerializer.fromXml(content);
        String json = buildJson(shapes);
        return prettyJson ? json : json.replace("\n", "").replace("  ", "");
    }

    @Override
    public String processAfterLoad(String content) {
        return jsonToXml(content);
    }

    @Override
    public JPanel createSettingsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox prettyCheck = new JCheckBox("Pretty JSON", prettyJson);
        prettyCheck.addActionListener(e -> prettyJson = prettyCheck.isSelected());
        panel.add(prettyCheck);
        return panel;
    }

    private String buildJson(List<AbstractShape> shapes) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        builder.append("  \"format\": \"shape-json-v1\",\n");
        builder.append("  \"xmlPayload\": \"");
        builder.append(escapeJson(ShapeXmlSerializer.prettyFormat(ShapeXmlSerializer.toXml(shapes))));
        builder.append("\"\n");
        builder.append("}");
        return builder.toString();
    }

    private String jsonToXml(String json) {
        Pattern pattern = Pattern.compile("\"xmlPayload\"\\s*:\\s*\"(.*)\"\\s*\\}", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(json);
        if (!matcher.find()) {
            throw new IllegalArgumentException("JSON payload from XML transform plugin is invalid.");
        }
        return unescapeJson(matcher.group(1));
    }

    private String escapeJson(String input) {
        return input
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    private String unescapeJson(String input) {
        return input
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
    }
}
