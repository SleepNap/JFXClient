package org.jc.main.core.update;

public class UpdateManager {
    private static UpdateManager instance;

    private UpdateManager() {
        initialize();
    }

    public static UpdateManager getInstance() {
        if (instance == null) {
            instance = new UpdateManager();
        }
        return instance;
    }

    public void initialize() {

    }

    public void checkUpdate() {

    }
}
