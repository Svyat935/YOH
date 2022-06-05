package com.yoh.backend.entity;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
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


    @Column(name = "status", length = 128, nullable = false)
    private String status;

    public String getStatus(){
        return this.status;
    }

    public void setStatus(String status){
        this.status = status;
    }


    @OneToMany(mappedBy = "result")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Answer> answers = new ArrayList<>();

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
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
