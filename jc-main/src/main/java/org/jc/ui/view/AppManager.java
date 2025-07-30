package org.jc.ui.view;

import org.jc.core.auth.AuthManager;
import org.jc.core.config.ConfigManager;

public class AppManager {
    private static AppManager instance;

    private AppManager() {
        initialize();
    }

    public static AppManager getInstance() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    private void initialize() {

    }

    public void beforeStart() {
        ConfigManager.getInstance().loadStartConfig();

    }

    public void starting() {

    }

    public void afterStart() {
        if (!AuthManager.getInstance().isAuthenticated()) {

        }
    }
}
