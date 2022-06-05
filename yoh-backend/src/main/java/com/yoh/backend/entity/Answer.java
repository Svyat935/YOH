package com.yoh.backend.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "answers")
public class Answer {
    public Answer() {}


    @Id
    @GeneratedValue
    private UUID id;

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    @ManyToOne(fetch = FetchType.EAGER)
    private Result result;

    public Result getResult(){
        return this.result;
    }

    public void setResult(Result result){
        this.result = result;
    }


    @ManyToOne(fetch = FetchType.EAGER)
    private Question question;

    public Question getQuestion(){
        return this.question;
    }

    public void setQuestion(Question question){
        this.question = question;
    }


    @Column(name = "answer", length = 128, nullable = false)
    private String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


    @Column(name = "score", length = 128, nullable = false)
    private String score;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
