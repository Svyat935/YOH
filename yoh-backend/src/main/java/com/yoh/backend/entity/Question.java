package com.yoh.backend.entity;

import javax.persistence.*;
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

}
