package com.yoh.backend.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    public User() {}

    public User(String login, String email, String password) {
        this.login = login;
        this.email = email;
        this.password = password;
    }


    @Id
    @GeneratedValue
    private UUID id;

    public UUID getId(){
        return this.id;
    }

    @Column(name = "login", length = 128, nullable = false)
    private String login;

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Column(name = "email", length = 128, nullable = false)
    private String email;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "password", nullable = false)
    private String password;

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }


}
