package com.yoh.backend.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "results")
public class Result {
    public Result() {}


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
    private Test test;

    public Test getTest(){
        return this.test;
    }

    public void setTest(Test test){
        this.test = test;
    }


    @ManyToOne
    private Patient patient;

    public Patient getPatient(){
        return this.patient;
    }

    public void setPatient(Patient patient){
        this.patient = patient;
    }


    @Column(name = "totalScore", length = 128, nullable = false)
    private String totalScore;

    public String getTotalScore(){
        return this.totalScore;
    }

    public void setTotalScore(String totalScore){
        this.totalScore = totalScore;
    }
}
