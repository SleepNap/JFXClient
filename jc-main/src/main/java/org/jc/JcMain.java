package org.jc;

import javafx.application.Application;
import org.jc.common.AppContext;

public class JcMain {
    public static void main(String[] args) {
        System.setProperty("prism.allowhidpi", "false");
        AppContext.INSTANCE.runArgs = args;
        Application.launch(JcApp.class, args);
    }
}