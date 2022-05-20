package com.yoh.backend.entity;

import com.yoh.backend.enums.GamePatientStatus;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "gamePatient")
public class GamePatient {
    public GamePatient() {}

    public GamePatient(Game game, Patient patient, GamePatientStatus gamePatientStatus){
        this.game = game;
        this.patient = patient;
        this.gamePatientStatus = gamePatientStatus;
    }

    @Id
    @GeneratedValue
    private UUID id;

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @ManyToOne
    private Game game;

    public Game getGame(){
        return this.game;
    }

    public void setGame(Game game){
        this.game = game;
    }

    @ManyToOne
    private Patient patient;

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }


    private GamePatientStatus gamePatientStatus;

    public GamePatientStatus getGamePatientStatus() {
        return gamePatientStatus;
    }

    public void setGamePatientStatus(GamePatientStatus gamePatientStatus) {
        this.gamePatientStatus = gamePatientStatus;
    }
}
