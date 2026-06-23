package pluginsystem;

import processing.DataProcessingPlugin;
import processing.LegacyPluginAdapter;
import processing.LegacyTextPlugin;
import processing.plugins.ChecksumPlugin;
import processing.plugins.LegacyShiftCipherPlugin;
import processing.plugins.XmlToJsonTransformPlugin;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Loads data processing plugins from built-ins, folder jars, and selected file.
 */
public final class ProcessingPluginLoader {
    private ProcessingPluginLoader() {
    }

    /**
     * Returns built-in plugins required by labs.
     */
    public static List<DataProcessingPlugin> loadBuiltIns() {
        List<DataProcessingPlugin> plugins = new ArrayList<>();
        plugins.add(new XmlToJsonTransformPlugin());
        plugins.add(new ChecksumPlugin());
        plugins.add(new LegacyPluginAdapter(new LegacyShiftCipherPlugin()));
        return plugins;
    }

    /**
     * Automatically loads plugin jars from folder.
     */
    public static List<DataProcessingPlugin> loadFromFolder(String pluginFolder) {
        List<DataProcessingPlugin> plugins = new ArrayList<>();
        Path pluginsPath = Path.of(pluginFolder);
        if (!Files.isDirectory(pluginsPath)) {
            return plugins;
        }
        try {
            Files.list(pluginsPath)
                    .filter(path -> path.toString().endsWith(".jar"))
                    .forEach(path -> plugins.addAll(loadFromJar(path)));
        } catch (Exception ignored) {
            // Ignore plugin scanning failures and keep app operational.
        }
        return plugins;
    }

    /**
     * Loads plugins from manually selected jar file.
     */
    public static List<DataProcessingPlugin> loadFromJar(Path jarPath) {
        List<DataProcessingPlugin> plugins = new ArrayList<>();
        try {
            URLClassLoader classLoader = new URLClassLoader(
                    new URL[]{jarPath.toUri().toURL()},
                    ProcessingPluginLoader.class.getClassLoader()
            );

            ServiceLoader<DataProcessingPlugin> modern = ServiceLoader.load(DataProcessingPlugin.class, classLoader);
            for (DataProcessingPlugin plugin : modern) {
                plugins.add(plugin);
            }

            ServiceLoader<LegacyTextPlugin> legacy = ServiceLoader.load(LegacyTextPlugin.class, classLoader);
            for (LegacyTextPlugin legacyPlugin : legacy) {
                plugins.add(new LegacyPluginAdapter(legacyPlugin));
            }
        } catch (Exception ignored) {
            // Ignore broken external plugins.
        }
        return plugins;
    }
}
