package pluginsystem;

import shapes.ShapeFactory;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Loads shape factories from plugin jars and optional class names.
 */
public final class PluginLoader {
    private PluginLoader() {
    }

    /**
     * Loads plugins from folder and command line class names.
     */
    public static List<ShapeFactory> loadFactories(String pluginFolder, String[] pluginClassNames) {
        List<ShapeFactory> loadedFactories = new ArrayList<>();
        loadedFactories.addAll(loadFromFolder(pluginFolder));
        loadedFactories.addAll(loadFromClassNames(pluginClassNames));
        return loadedFactories;
    }

    /**
     * Loads shape factories from a manually selected plugin jar.
     */
    public static List<ShapeFactory> loadFactoriesFromJar(Path jarPath) {
        List<ShapeFactory> factories = new ArrayList<>();
        try {
            URLClassLoader classLoader = new URLClassLoader(
                    new URL[]{jarPath.toUri().toURL()},
                    PluginLoader.class.getClassLoader()
            );
            ServiceLoader<ShapeFactory> serviceLoader = ServiceLoader.load(ShapeFactory.class, classLoader);
            for (ShapeFactory factory : serviceLoader) {
                factories.add(factory);
            }
        } catch (Exception ignored) {
            // Ignore broken jar and return empty list.
        }
        return factories;
    }

    private static List<ShapeFactory> loadFromFolder(String pluginFolder) {
        List<ShapeFactory> factories = new ArrayList<>();
        Path pluginsPath = Path.of(pluginFolder);
        if (!Files.isDirectory(pluginsPath)) {
            return factories;
        }

        try {
            List<URL> jarUrls = new ArrayList<>();
            // Load all jars from folder; signature check is skipped so that
            // third-party plugins provided by teammates can be used without
            // needing a .sig file signed with this project's private key.
            Files.list(pluginsPath)
                    .filter(path -> path.toString().endsWith(".jar"))
                    .forEach(path -> {
                        try {
                            jarUrls.add(path.toUri().toURL());
                        } catch (Exception ignored) {
                            // Ignore malformed plugin URLs and continue scanning.
                        }
                    });

            if (jarUrls.isEmpty()) {
                return factories;
            }

            URLClassLoader classLoader = new URLClassLoader(
                    jarUrls.toArray(new URL[0]),
                    PluginLoader.class.getClassLoader()
            );
            ServiceLoader<ShapeFactory> serviceLoader = ServiceLoader.load(ShapeFactory.class, classLoader);
            for (ShapeFactory factory : serviceLoader) {
                factories.add(factory);
            }
        } catch (IOException ignored) {
            // Ignore plugin scanning errors so base app still works.
        }

        return factories;
    }

    private static List<ShapeFactory> loadFromClassNames(String[] pluginClassNames) {
        List<ShapeFactory> factories = new ArrayList<>();
        if (pluginClassNames == null) {
            return factories;
        }

        for (String className : pluginClassNames) {
            try {
                Class<?> clazz = Class.forName(className);
                if (ShapeFactory.class.isAssignableFrom(clazz)) {
                    ShapeFactory factory = (ShapeFactory) clazz.getDeclaredConstructor().newInstance();
                    factories.add(factory);
                }
            } catch (Exception ignored) {
                // Ignore bad runtime plugin class names.
            }
        }
        return factories;
    }
}