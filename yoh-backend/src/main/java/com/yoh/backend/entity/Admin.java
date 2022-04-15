package com.yoh.backend.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "admins")
public class Admin {
    public Admin() {}

    public Admin(User user){
        this.user = user;
    }


    @Id
    @GeneratedValue
    private UUID id;

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser(){
        return this.user;
    }

    public void setUser(User user){
        this.user = user;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", user=" + user +
                '}';
    }
}
