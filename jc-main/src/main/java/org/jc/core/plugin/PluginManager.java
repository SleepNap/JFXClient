package org.jc.core.plugin;

public class PluginManager {
    private static PluginManager instance;

    private PluginManager() {
        initialize();
    }

    public static PluginManager getInstance() {
        if (instance == null) {
            instance = new PluginManager();
        }
        return instance;
    }

    private void initialize() {
        loadPlugins();
    }

    private void loadPlugins() {

    }
}
