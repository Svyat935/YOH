package com.yoh.backend.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tests")
public class Test {
    public Test() {}


    @Id
    @GeneratedValue
    private UUID id;

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    @Column(name = "subject", length = 128, nullable = false)
    private String subject;

    public String getSubject(){
        return this.subject;
    }

    public void setSubject(String subject){
        this.subject = subject;
    }

    @Column(name = "dateAdding", nullable = false)
    private LocalDateTime dateAdding;

    public LocalDateTime getDateAdding() {
        return dateAdding;
    }

    public void setDateAdding(LocalDateTime dateAdding) {
        this.dateAdding = dateAdding;
    }

    @ManyToMany
    private List<Question> questions;

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

}
