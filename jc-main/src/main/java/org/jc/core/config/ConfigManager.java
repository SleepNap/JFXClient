package org.jc.core.config;

public class ConfigManager {
    private static ConfigManager instance;

    private ConfigManager() {
        initialize();
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    private void initialize() {

    }

    public void loadStartConfig() {

    }

    public void loadContextConfig() {

    }
}
