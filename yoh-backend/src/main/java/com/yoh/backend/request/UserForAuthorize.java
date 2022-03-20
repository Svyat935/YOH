package com.yoh.backend.request;

import javax.validation.constraints.NotNull;

public class UserForAuthorize {
    @NotNull
    private String credentials;

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String login) {
        this.credentials = login;
    }

    @NotNull
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
