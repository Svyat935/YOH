package com.yoh.backend.request;

import javax.validation.constraints.NotNull;

public class TestToPatient {
    @NotNull
    private String test_id;

    public String getTest_id() {
        return test_id;
    }

    public void setTest_id(String game_id) {this.test_id = test_id;}

    @NotNull
    private String patient_id;

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {this.patient_id = patient_id;}
}
