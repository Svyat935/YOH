package com.yoh.backend.request;

import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

public class OrganizationForAdding {

    @NotNull
    private String name;

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }


    @Nullable
    private String address;

    public String getAddress(){
        return this.address;
    }

    public void setAddress(String address){
        this.address = address;
    }


    @Nullable
    private String phone;

    public String getPhone(){
        return this.phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }


    @Nullable
    private String email;

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String email){
        this.email = email;
    }


    @Nullable
    private String website;

    public String getWebsite(){
        return this.website;
    }

    public void setWebsite(String website){
        this.website = website;
    }
}
