//package com.yoh.backend.entity;
//
//import javax.persistence.*;
//import java.util.UUID;
//
//@Entity
//@Table(name = "testStatus")
//public class TestStatus {
//
//    public TestStatus() { }
//
//    public TestStatus(Test test, Patient patient, String status) {
//        this.test = test;
//        this.patient = patient;
//        this.status = status;
//    }
//
//
//    @Id
//    @GeneratedValue
//    private UUID id;
//
//    public UUID getId() {
//        return this.id;
//    }
//
//    public void setId(UUID id) {
//        this.id = id;
//    }
//
//
//    @ManyToOne
//    private Test test;
//
//    public Test getTest(){
//        return this.test;
//    }
//
//    public void setTest(Test test){
//        this.test = test;
//    }
//
//
//    @ManyToOne
//    private Patient patient;
//
//    public Patient getPatient(){
//        return this.patient;
//    }
//
//    public void setPatient(Patient patient){
//        this.patient = patient;
//    }
//
//
//    @Column(name = "status", length = 128, nullable = false)
//    private String status;
//
//    public String getStatus(){
//        return this.status;
//    }
//
//    public void setStatus(String status){
//        this.status = status;
//    }
//}