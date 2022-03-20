package com.yoh.backend.response;

import com.yoh.backend.entity.Patient;

import javax.validation.constraints.NotNull;

public class PatientListResponse {

    private final String id;
    private final String name;
    private final String surname;
    private final String organization;

    public PatientListResponse(String id, String name, String surname, String organization){
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.organization = organization;
    }

    public PatientListResponse(Patient patient) {
        this.id = patient.getId().toString();
        this.name = patient.getName();
        this.surname = patient.getSurname();
        this.organization = patient.getOrganization().getId().toString();
    }

    public String getId() { return id; }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getOrganization() {
        return organization;
    }
}
