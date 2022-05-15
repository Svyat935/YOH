package com.yoh.backend.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import org.hibernate.annotations.Type;

import javax.json.bind.Jsonb;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "gameStatistics")
public class GameStatistic {
    public GameStatistic() {}

    public GameStatistic(Game game, Patient patient, Short type, LocalDateTime dateAction, Short answerNumber, Jsonb details) {
        //TODO
        this.game = game;
        this.patient = patient;
        this.type = type;
        this.dateAction = dateAction;
        this.answerNumber = answerNumber;
        this.details = details;
//        this.message = null;
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


    @Column(name = "type", nullable = false)
    private Short type;

    public Short getType(){
        return this.type;
    }

    public void setType(Short type){
        this.type = type;
    }


    @Column(name = "dateAction", nullable = false)
    private LocalDateTime dateAction;

    public LocalDateTime getDateAction(){
        return this.dateAction;
    }

    public void setDateAction(LocalDateTime dateAction){
        this.dateAction = dateAction;
    }


    @Column(name = "answerNumber", nullable = true)
    private Short answerNumber;

    public Short getAnswerNumber() {
        return this.answerNumber;
    }

    public void setAnswerNumber(Short answerNumber) {
        this.answerNumber = answerNumber;
    }


//    @Type(type = "jsonb")
    @Transient
    @Column(name = "details", nullable = true, columnDefinition = "jsonb")
    private Jsonb details;

    public Jsonb getDetails() {
        return this.details;
    }

    public void setDetails(Jsonb details) {
        this.details = details;
    }

    //    @Column(name = "message", length = 128, nullable = true)
//    private String message;
//
//    public String getMessage(){
//        return this.message;
//    }
//
//    public void setMessage(String message){
//        this.message = message;
//    }

//
//    ID Пользователя
//    ID игры
//    DateAction - datetime with time zone
//    Type - smallint
//    Message - text
}
