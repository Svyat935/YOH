package com.yoh.backend.request;

import org.springframework.lang.Nullable;

public class EditTutorInfoRequest {
    @Nullable
    private String name;

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    @Nullable
    private String surname;

    public String getSurname() { return surname; }

    public void setSurname(String surname) {this.surname = surname;}

    @Nullable
    private String secondName;

    public String getSecondName() { return secondName; }

    public void setSecondName(String secondName) {this.secondName = secondName;}


    @Nullable
    private String organization;

    public String getOrganization() { return organization; }

    public void setOrganization(String organization) {this.organization = organization;}
}
