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

    @Column(name = "surname", length = 128, nullable = true)
    private String surname;

    public String getSurname(){
        return this.surname;
    }

    public void setSurname(String surname){
        this.surname = surname;
    }

    @Column(name = "name", length = 128, nullable = true)
    private String name;

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    @Column(name = "secondName", length = 128, nullable = true)
    private String secondName;

    public String getSecondName(){
        return this.secondName;
    }

    public void setSecondName(String secondName){
        this.secondName = secondName;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", user=" + user +
                '}';
    }
}
