package com.yoh.backend.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "testStatistics")
public class TestStatistic {
    public TestStatistic() {}

    public TestStatistic(Test test, Patient patient, Short type, Date dateAction, String message) {
        this.test = test;
        this.patient = patient;
        this.type = type;
        this.dateAction = dateAction;
        this.message = message;
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


    @Column(name = "type", nullable = false)
    private Short type;

    public Short getType(){
        return this.type;
    }

    public void setType(Short type){
        this.type = type;
    }


    @Column(name = "dateAction", nullable = false)
    private Date dateAction;

    public Date getDateAction(){
        return this.dateAction;
    }

    public void setDateAction(Date dateAction){
        this.dateAction = dateAction;
    }



    @Column(name = "message", length = 128, nullable = false)
    private String message;

    public String getMessage(){
        return this.message;
    }

    public void setMessage(String message){
        this.message = message;
    }

//
//    ID Пользователя
//    ID теста
//    DateAction - datetime with time zone
//    Type - smallint
//    Message - text
}
