package org.jc.common;

import javafx.stage.Stage;

import java.util.Locale;

public class AppContext {
    public static final AppContext INSTANCE = new AppContext();

    // ***** system *****
    public String[] runArgs;
    public Locale locale = Locale.getDefault();

    // ***** ui *****
    public Stage primaryStage;
}
