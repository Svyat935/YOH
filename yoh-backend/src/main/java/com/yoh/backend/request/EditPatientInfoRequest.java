package com.yoh.backend.request;

import org.springframework.lang.Nullable;

public class EditPatientInfoRequest {
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
    private String gender;

    public String getGender() { return gender; }

    public void setGender(String gender) {this.gender = gender;}

//    @Nullable
//    private String organization;
//
//    public String getOrganization() { return organization; }
//
//    public void setOrganization(String organization) {this.organization = organization;}

    @Nullable
    private String birthDate;

    public String getBirthDate() { return birthDate; }

    public void setBirthDate(String birthDate) {this.birthDate = birthDate;}

    @Nullable
    private String numberPhone;

    public String getNumberPhone() { return numberPhone; }

    public void setNumberPhone(String numberPhone) {this.numberPhone = numberPhone;}

    @Nullable
    private String address;

    public String getAddress() { return address; }

    public void setAddress(String address) {this.address = address;}
}
