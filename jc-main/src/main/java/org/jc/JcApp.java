package org.jc;

import javafx.application.Application;
import javafx.stage.Stage;
import org.jc.common.AppContext;
import org.jc.core.auth.AuthManager;
import org.jc.core.config.ConfigManager;
import org.jc.ui.view.AppManager;

public class JcApp extends Application {
    @Override
    public void init() throws Exception {
        AppManager.getInstance().beforeStart();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        AppContext.INSTANCE.primaryStage = primaryStage;
        AppManager.getInstance().starting();
        AppManager.getInstance().afterStart();
    }

    @Override
    public void stop() throws Exception {

    }
}
