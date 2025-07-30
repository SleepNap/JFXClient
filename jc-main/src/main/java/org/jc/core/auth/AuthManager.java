package org.jc.core.auth;

public class AuthManager {
    private static AuthManager instance;

    private AuthManager() {
        initialize();
    }

    public static AuthManager getInstance() {
        if (instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }

    public void initialize() {

    }

    public boolean isAuthenticated() {
        return true;
    }
}
