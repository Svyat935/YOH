package com.yoh.backend.request;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

public class GamesToPatient {
    @NotNull
    private ArrayList<String> games_id;

    public ArrayList<String> getGames_id() {
        return games_id;
    }

    public void setGames_id(ArrayList<String> games_id) {this.games_id = games_id;}

    @NotNull
    private String patient_id;

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {this.patient_id = patient_id;}
}
