package org.jc.main.ui.view;

import org.jc.common.exception.UnauthedException;
import org.jc.main.core.auth.AuthManager;
import org.jc.main.core.config.ConfigManager;
import org.jc.main.core.database.DatabaseManager;

public class AppManager {
    private static AppManager instance;
    private Thread startThread;

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
        startThread = Thread.startVirtualThread(() -> {
            ConfigManager.getInstance().loadStartConfig();
            DatabaseManager.getInstance().initDataSource();
        });
    }

    public void starting() {

    }

    public void afterStart() {
        if (!AuthManager.getInstance().isAuthenticated()) {
            throw new UnauthedException("Not authenticated");
        }
    }

    public void beforeStop() {
        if (startThread.isAlive()) {
            startThread.interrupt();
        }
        DatabaseManager.getInstance().stopDatabase();
        System.exit(0);
    }
}
