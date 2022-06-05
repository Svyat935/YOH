package com.yoh.backend.entity;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "questions")
public class Question {
    public Question() {}


    @Id
    @GeneratedValue
    private UUID id;

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    @Column(name = "formulation", length = 128, nullable = false)
    private String formulation;

    public String getFormulation(){
        return this.formulation;
    }

    public void setFormulation(String formulation){
        this.formulation = formulation;
    }


    @Column(name = "rightAnswer", length = 128, nullable = false)
    private String rightAnswer;

    public String getRightAnswer(){
        return this.rightAnswer;
    }

    public void setRightAnswer(String rightAnswer){
        this.rightAnswer = rightAnswer;
    }


    @Column(name = "score", length = 128, nullable = false)
    private String score;

    public String getScore(){
        return this.score;
    }

    public void setScore(String score){
        this.score = score;
    }

    @OneToMany(mappedBy = "question")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Answer> answers = new ArrayList<>();

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }


    @ManyToMany
    private List<Test> tests;

    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }

}
