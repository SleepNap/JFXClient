package org.jc.core.module;

public class ModuleManager {
    private static ModuleManager instance;

    private ModuleManager() {
        initialize();
    }

    public static ModuleManager getInstance() {
        if (instance == null) {
            instance = new ModuleManager();
        }
        return instance;
    }

    private void initialize() {

    }

    public void loadModules() {

    }
}
