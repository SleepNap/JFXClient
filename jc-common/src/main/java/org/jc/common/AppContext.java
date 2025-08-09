package org.jc.common;

import javafx.stage.Stage;
import org.jc.common.factory.ConfigFactory;
import org.jc.common.factory.DatabaseFactory;

import java.util.Locale;

public class AppContext {
    public static final AppContext INSTANCE = new AppContext();

    // ***** system *****
    public String[] runArgs;
    public Locale locale = Locale.getDefault();

    // ***** ui *****
    public Stage primaryStage;

    // ***** factory *****
    public ConfigFactory configFactory;
    public DatabaseFactory databaseFactory;
}
