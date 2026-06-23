package processing;

import javax.swing.*;
import java.awt.*;

/**
 * Adapter pattern implementation that wraps legacy text plugin API.
 */
public class LegacyPluginAdapter implements DataProcessingPlugin {
    private final LegacyTextPlugin legacyPlugin;

    public LegacyPluginAdapter(LegacyTextPlugin legacyPlugin) {
        this.legacyPlugin = legacyPlugin;
    }

    @Override
    public String getPluginId() {
        return "legacy-adapter-" + legacyPlugin.getClass().getSimpleName();
    }

    @Override
    public String getDisplayName() {
        return "Adapter: " + legacyPlugin.getName();
    }

    @Override
    public String processBeforeSave(String content) {
        return legacyPlugin.encode(content);
    }

    @Override
    public String processAfterLoad(String content) {
        return legacyPlugin.decode(content);
    }

    @Override
    public JPanel createSettingsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Shift:"));
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(legacyPlugin.getShift(), 1, 16, 1));
        spinner.addChangeListener(e -> legacyPlugin.setShift((Integer) spinner.getValue()));
        panel.add(spinner);
        return panel;
    }
}
