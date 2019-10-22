package com.disp_mov.tacos.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String appToken;
    private String displayName;

    public LoggedInUser(String appToken, String displayName) {
        this.appToken = appToken;
        this.displayName = displayName;
    }

    public String getToken() {
        return appToken;
    }

    public String getDisplayName() {
        return displayName;
    }
}
