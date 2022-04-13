package com.yoh.backend.request;

import javax.validation.constraints.NotNull;

public class RoleForAssign {

    @NotNull
    private Integer role;

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    @NotNull
    private String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
