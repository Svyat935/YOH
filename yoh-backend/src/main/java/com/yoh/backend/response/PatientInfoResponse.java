package com.yoh.backend.response;

import com.yoh.backend.entity.Patient;

import javax.validation.constraints.NotNull;

public class PatientInfoResponse {

    private final String id;
    private final String name;
    private final String surname;
    private final String secondName;
    private final String gender;
    private final String organization;
    private final String birthDate;
    private final String numberPhone;
    private final String address;
    private final TutorInfoResponse tutor;

    public PatientInfoResponse(Patient patient) {
        this.id = patient.getId().toString();
        this.name = patient.getName();
        this.surname = patient.getSurname();
        this.secondName = patient.getSecondName();
        this.gender = patient.getGender().toString();
        this.organization = patient.getOrganization().getId().toString();
        this.birthDate = patient.getBirthDate();
        this.numberPhone = patient.getNumberPhone();
        this.address = patient.getAddress();
        this.tutor = new TutorInfoResponse(patient.getTutor());
    }

    public String getId() { return id; }

    public String getName() { return name; }

    public String getSurname() { return surname; }

    public String getSecondName() { return secondName; }

    public String getGender() { return gender; }

    public String getOrganization() { return organization; }

    public String getBirthDate() { return birthDate; }

    public String getNumberPhone() { return numberPhone; }

    public String getAddress() { return address; }

    public TutorInfoResponse getTutor() { return tutor; }
}
