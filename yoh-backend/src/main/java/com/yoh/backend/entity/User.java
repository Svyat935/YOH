package com.yoh.backend.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    public User() {}

    public User(String login, String email, String password, LocalDateTime dateRegistration, Integer role) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.dateRegistration = dateRegistration;
        this.role = role;
    }

    public User(String login, String email, String password, Integer role) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.role = role;
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

    @Column(name = "role", nullable = true)
    private Integer role;

    public Integer getRole() {
        return this.role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    //TODO Сменить на nullable false после полной очистки бд
    @Column(name = "dateRegistration", nullable = true)
    private LocalDateTime dateRegistration;

    public LocalDateTime getDateRegistration() {
        return this.dateRegistration;
    }

    public void setDateRegistration(LocalDateTime dateRegistration) {
        this.dateRegistration = dateRegistration;
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
