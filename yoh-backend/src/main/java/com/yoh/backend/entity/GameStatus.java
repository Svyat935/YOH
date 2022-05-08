package com.yoh.backend.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "gameStatus")
public class GameStatus {

    public GameStatus() { }

    public GameStatus(Game game, Patient patient, Tutor tutor, LocalDateTime assignmentDate, String status) {
        this.game = game;
        this.patient = patient;
        this.tutor = tutor;
        this.assignmentDate = assignmentDate;
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


    @ManyToOne(fetch = FetchType.EAGER)
    private Patient patient;

    public Patient getPatient(){
        return this.patient;
    }

    public void setPatient(Patient patient){
        this.patient = patient;
    }


    @ManyToOne
    private Tutor tutor;

    public Tutor getTutor() {
        return this.tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }


    @Column(name = "assignmentDate", nullable = true)
    private LocalDateTime assignmentDate;

    public LocalDateTime getAssignmentDate() {
        return this.assignmentDate;
    }

    public void setAssignmentDate(LocalDateTime assignmentDate) {
        this.assignmentDate = assignmentDate;
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
