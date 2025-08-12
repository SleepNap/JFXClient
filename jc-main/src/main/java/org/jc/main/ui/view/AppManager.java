package org.jc.main.ui.view;

import javafx.scene.text.Font;
import lombok.extern.slf4j.Slf4j;
import org.jc.common.AppContext;
import org.jc.common.exception.UnauthedException;
import org.jc.main.core.auth.AuthManager;
import org.jc.main.core.config.ConfigManager;
import org.jc.main.core.database.DatabaseManager;

import java.io.File;

@Slf4j
public class AppManager {
    private static AppManager instance;
    private Thread startThread;
    private File fontDir;

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
        fontDir = new File("resources/font");
    }

    public void beforeStart() {
        startThread = Thread.startVirtualThread(() -> {
            ConfigManager.getInstance().loadStartConfig();
            DatabaseManager.getInstance().initDataSource();

            loadFont();
        });
    }

    public void starting() {
        try {
            startThread.join();
        } catch (InterruptedException e) {
            // todo show start error confirm and exit
            log.error(e.getMessage());
            System.exit(1);
            return;
        }
        AppContext.INSTANCE.primaryStage.show();
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

    public void loadFont() {
        if (!fontDir.exists()) {
            return;
        }
        File[] fontFiles = fontDir.listFiles();
        if (fontFiles == null) {
            return;
        }
        for (File file : fontFiles) {
            if (!file.isFile()) {
                continue;
            }
            Font.loadFont(file.toURI().toString(), -1);
            log.debug("load font: {}", file.getName());
        }
    }
}
