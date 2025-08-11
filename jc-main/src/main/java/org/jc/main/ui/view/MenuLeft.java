package org.jc.main.ui.view;

import javafx.scene.layout.BorderPane;

public class MenuLeft extends BorderPane {

    public MenuLeft() {
        initialize();
    }

    private void initialize() {
        MenuLeftCenter menuLeftCenter = new MenuLeftCenter();
        MenuLeftBottom menuLeftBottom = new MenuLeftBottom();
        setCenter(menuLeftCenter);
        setBottom(menuLeftBottom);
    }
}
