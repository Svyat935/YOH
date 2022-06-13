package com.yoh.backend.request;

import javax.validation.constraints.NotNull;

public class ChangeEmailRequest {
    @NotNull
    private String user_id;

    public String getUser_id() { return user_id;}

    public void setUser_id(String user_id) { this.user_id = user_id;}


    @NotNull
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
