package org.jc.common.api;

import javafx.scene.Node;

public interface ModuleInterface {
    String getModuleName();
    int getPriority();
    Node getIcon();
    void openModule();
    void closeModule();
}
