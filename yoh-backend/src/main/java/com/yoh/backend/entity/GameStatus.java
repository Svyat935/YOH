package com.yoh.backend.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "gameStatus")
public class GameStatus {

    public GameStatus() { }

    public GameStatus(Game game, Patient patient, String status) {
        this.game = game;
        this.patient = patient;
        this.status = status;
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

    public Patient getPatient(){
        return this.patient;
    }

    public void setPatient(Patient patient){
        this.patient = patient;
    }


    @Column(name = "status", length = 128, nullable = false)
    private String status;

    public String getStatus(){
        return this.status;
    }

    public void setStatus(String status){
        this.status = status;
    }
}
