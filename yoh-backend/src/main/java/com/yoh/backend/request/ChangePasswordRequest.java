package com.yoh.backend.request;

import javax.validation.constraints.NotNull;

public class ChangePasswordRequest {
    @NotNull
    private String user_id;

    public String getUser_id() { return user_id;}

    public void setUser_id(String user_id) { this.user_id = user_id;}


    @NotNull
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
