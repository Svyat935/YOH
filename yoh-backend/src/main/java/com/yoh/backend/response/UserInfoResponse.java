package com.yoh.backend.response;

import com.yoh.backend.entity.User;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

public class UserInfoResponse {

    //TODO навести красоту

    public UserInfoResponse(User user){
        setId(user.getId().toString());
        setLogin(user.getLogin());
        setEmail(user.getEmail());
        setRole(user.getRole());
        setRoleString(user.getRole());

    }

    private String id;

    public String getId(){
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String login;

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    private String email;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private Integer role;

    public Integer getRole() {
        return this.role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    private String roleString;

    public String getRoleString() {
        return this.roleString;
    }

    public void setRoleString(String roleString) {
        this.roleString = roleString;
    }

    public void setRoleString(Integer role) {
        if (role != null)
            this.roleString = role == 0 ? "Admin" :
                    role == 1 ? "Patient" :
                            role == 2 ? "Researcher" : "Tutor";
    }
}
