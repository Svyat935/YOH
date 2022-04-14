package com.yoh.backend.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "organizations")
public class Organization {
    public Organization() {}

    public Organization(String name, String address, String phone, String email, String website) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.website = website;
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


    @Column(name = "name", length = 128, nullable = false)
    private String name;

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }


    @Column(name = "address", length = 128, nullable = true)
    private String address;

    public String getAddress(){
        return this.address;
    }

    public void setAddress(String address){
        this.address = address;
    }


    @Column(name = "phone", length = 128, nullable = true)
    private String phone;

    public String getPhone(){
        return this.phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }


    @Column(name = "email", length = 128, nullable = true)
    private String email;

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String email){
        this.email = email;
    }


    @Column(name = "website", length = 128, nullable = true)
    private String website;

    public String getWebsite(){
        return this.website;
    }

    public void setWebsite(String website){
        this.website = website;
    }

}
