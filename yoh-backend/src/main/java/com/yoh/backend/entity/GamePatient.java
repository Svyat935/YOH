package com.yoh.backend.entity;

import com.yoh.backend.enums.GameActiveStatus;
import com.yoh.backend.enums.GamePatientStatus;
import com.yoh.backend.enums.Status;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "gamePatient")
public class GamePatient {
    public GamePatient() {}

    public GamePatient(Game game, Patient patient, Tutor tutor, LocalDateTime assignmentDate, GamePatientStatus gamePatientStatus, Status status){
        this.game = game;
        this.patient = patient;
        this.tutor = tutor;
        this.assignmentDate = assignmentDate;
        this.gamePatientStatus = gamePatientStatus;
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

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
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


    @Enumerated(EnumType.STRING)
    @Column(name = "gamePatientStatus", nullable = false)
    private GamePatientStatus gamePatientStatus;

    public GamePatientStatus getGamePatientStatus() {
        return gamePatientStatus;
    }

    public void setGamePatientStatus(GamePatientStatus gamePatientStatus) {
        this.gamePatientStatus = gamePatientStatus;
    }


    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    public Status getStatus(){
        return this.status;
    }

    public void setStatus(Status status){
        this.status = status;
    }
}
