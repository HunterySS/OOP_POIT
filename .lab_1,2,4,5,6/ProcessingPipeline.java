package processing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Chain of Responsibility that applies enabled plugins in sequence.
 */
public class ProcessingPipeline {
    private final List<PluginState> pluginStates = new ArrayList<>();

    /**
     * Stores plugin and enabled flag for UI-controlled execution.
     */
    public static class PluginState {
        private final DataProcessingPlugin plugin;
        private boolean enabled;

        public PluginState(DataProcessingPlugin plugin, boolean enabled) {
            this.plugin = plugin;
            this.enabled = enabled;
        }

        public DataProcessingPlugin getPlugin() {
            return plugin;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    /**
     * Adds plugin to chain and enables it by default.
     */
    public void addPlugin(DataProcessingPlugin plugin) {
        pluginStates.add(new PluginState(plugin, true));
    }

    /**
     * Applies save transformation pipeline from first to last plugin.
     */
    public String applyBeforeSave(String content) {
        String current = content;
        for (PluginState state : pluginStates) {
            if (state.isEnabled()) {
                current = state.getPlugin().processBeforeSave(current);
            }
        }
        return current;
    }

    /**
     * Applies load pipeline in reverse to undo save transformations.
     */
    public String applyAfterLoad(String content) {
        String current = content;
        List<PluginState> reversed = new ArrayList<>(pluginStates);
        Collections.reverse(reversed);
        for (PluginState state : reversed) {
            if (state.isEnabled()) {
                current = state.getPlugin().processAfterLoad(current);
            }
        }
        return current;
    }

    /**
     * Returns mutable states used by plugin settings dialog.
     */
    public List<PluginState> getPluginStates() {
        return pluginStates;
    }
}
