package com.yoh.backend.request;

import javax.validation.constraints.NotNull;

public class GameToPatient {
    @NotNull
    private String game_id;

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {this.game_id = game_id;}

    @NotNull
    private String patient_id;

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {this.patient_id = patient_id;}
}
