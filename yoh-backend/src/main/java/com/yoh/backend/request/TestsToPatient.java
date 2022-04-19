package com.yoh.backend.request;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

public class TestsToPatient {
    @NotNull
    private ArrayList<String> tests_id;

    public ArrayList<String> getTests_id() {
        return tests_id;
    }

    public void setTests_id(ArrayList<String> tests_id) {this.tests_id = tests_id;}

    @NotNull
    private String patient_id;

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {this.patient_id = patient_id;}
}
