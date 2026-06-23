package processing.plugins;

import processing.DataProcessingPlugin;

import javax.swing.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

/**
 * Adds checksum header before save and validates it after load.
 */
public class ChecksumPlugin implements DataProcessingPlugin {
    private String algorithm = "SHA-256";

    @Override
    public String getPluginId() {
        return "checksum-plugin";
    }

    @Override
    public String getDisplayName() {
        return "Checksum guard";
    }

    @Override
    public String processBeforeSave(String content) {
        String hash = digest(content, algorithm);
        return "#checksum:" + algorithm + ":" + hash + "\n" + content;
    }

    @Override
    public String processAfterLoad(String content) {
        String[] parts = content.split("\n", 2);
        if (parts.length < 2 || !parts[0].startsWith("#checksum:")) {
            throw new IllegalArgumentException("Checksum header was not found.");
        }

        String[] header = parts[0].split(":");
        if (header.length != 3) {
            throw new IllegalArgumentException("Checksum header has invalid format.");
        }

        String usedAlgorithm = header[1];
        String expectedHash = header[2];
        String payload = parts[1];
        String actualHash = digest(payload, usedAlgorithm);
        if (!actualHash.equalsIgnoreCase(expectedHash)) {
            throw new IllegalArgumentException("Checksum mismatch. File was modified or damaged.");
        }
        return payload;
    }

    @Override
    public JPanel createSettingsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Algorithm:"));
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"SHA-256", "SHA-512"});
        comboBox.setSelectedItem(algorithm);
        comboBox.addActionListener(e -> algorithm = (String) comboBox.getSelectedItem());
        panel.add(comboBox);
        return panel;
    }

    private String digest(String content, String digestAlgorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(digestAlgorithm);
            return HexFormat.of().formatHex(digest.digest(content.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to calculate checksum.", exception);
        }
    }
}
