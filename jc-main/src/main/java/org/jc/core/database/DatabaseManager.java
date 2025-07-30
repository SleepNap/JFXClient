package org.jc.core.database;

public class DatabaseManager {
    private static DatabaseManager instance;

    private DatabaseManager() {
        initialize();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void initialize() {

    }
}
