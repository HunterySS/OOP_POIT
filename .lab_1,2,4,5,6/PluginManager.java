package com.library.plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * Central plugin loader
 */
public final class PluginManager {

    private static final List<PluginDescriptor> ITEM_PLUGINS = new ArrayList<>();
    private static final List<FileProcessingPlugin> FILE_PROCESSING_PLUGINS = new ArrayList<>();
    private static final Set<String> LOADED_PLUGIN_KEYS = new LinkedHashSet<>();

    static {
        loadFromClassLoader(Thread.currentThread().getContextClassLoader());
    }

    private PluginManager() {
    }

    public static List<PluginDescriptor> getItemPlugins() {
        return Collections.unmodifiableList(ITEM_PLUGINS);
    }

    public static List<FileProcessingPlugin> getFileProcessingPlugins() {
        return Collections.unmodifiableList(FILE_PROCESSING_PLUGINS);
    }

    private static void loadFromClassLoader(ClassLoader classLoader) {
        ServiceLoader<PluginDescriptor> itemLoader = ServiceLoader.load(PluginDescriptor.class, classLoader);
        for (PluginDescriptor plugin : itemLoader) {
            addItemPlugin(plugin);
        }

        ServiceLoader<FileProcessingPlugin> processorLoader =
                ServiceLoader.load(FileProcessingPlugin.class, classLoader);
        for (FileProcessingPlugin plugin : processorLoader) {
            addFileProcessingPlugin(plugin);
        }
    }

    private static void addItemPlugin(PluginDescriptor plugin) {
        String key = "item:" + plugin.getClass().getName();
        if (LOADED_PLUGIN_KEYS.add(key)) {
            ITEM_PLUGINS.add(plugin);
            System.out.println("[Plugin] Item plugin loaded: " + plugin.getTypeName());
        }
    }

    private static void addFileProcessingPlugin(FileProcessingPlugin plugin) {
        String key = "processor:" + plugin.getClass().getName();
        if (LOADED_PLUGIN_KEYS.add(key)) {
            FILE_PROCESSING_PLUGINS.add(plugin);
            System.out.println("[Plugin] File processor loaded: " + plugin.getPluginName());
        }
    }
}