package processing;

import javax.swing.*;

/**
 * Strategy contract for data transformations around file persistence.
 */
public interface DataProcessingPlugin {
    /**
     * @return stable plugin id used in menu and state storage.
     */
    String getPluginId();

    /**
     * @return human-readable plugin display name.
     */
    String getDisplayName();

    /**
     * Called in save pipeline before writing data to disk.
     */
    String processBeforeSave(String content);

    /**
     * Called in load pipeline after file read and before XML parsing.
     */
    String processAfterLoad(String content);

    /**
     * @return settings panel or null if plugin has no extra settings.
     */
    JPanel createSettingsPanel();
}
