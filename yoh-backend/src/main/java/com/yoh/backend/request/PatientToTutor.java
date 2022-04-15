package com.yoh.backend.request;

import javax.validation.constraints.NotNull;

public class PatientToTutor {
    @NotNull
    private String patient;

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {this.patient = patient;}
}
