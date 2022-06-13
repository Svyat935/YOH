//package com.yoh.backend.entity;
//
//import javax.persistence.*;
//import java.time.LocalDateTime;
//import java.util.UUID;
//import com.yoh.backend.enums.Status;
//
//@Entity
//@Table(name = "gameStatus")
//public class GameStatus {
//
//    public GameStatus() { }
//
//    public GameStatus(GamePatient gamePatient, Tutor tutor, LocalDateTime assignmentDate, Status status) {
////        this.game = game;
////        this.patient = patient;
//        this.gamePatient = gamePatient;
//        this.tutor = tutor;
//        this.assignmentDate = assignmentDate;
//        this.status = status;
//    }
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
//    @OneToOne
//    private GamePatient gamePatient;
//
//    public GamePatient getGamePatient() {
//        return gamePatient;
//    }
//
//    public void setGamePatient(GamePatient gamePatient) {
//        this.gamePatient = gamePatient;
//    }
////    @ManyToOne
////    private Game game;
////
////    public Game getGame(){
////        return this.game;
////    }
////
////    public void setGame(Game game){
////        this.game = game;
////    }
////
////
////    @ManyToOne(fetch = FetchType.EAGER)
////    private Patient patient;
////
////    public Patient getPatient(){
////        return this.patient;
////    }
////
////    public void setPatient(Patient patient){
////        this.patient = patient;
////    }
//
//
//    @ManyToOne
//    private Tutor tutor;
//
//    public Tutor getTutor() {
//        return this.tutor;
//    }
//
//    public void setTutor(Tutor tutor) {
//        this.tutor = tutor;
//    }
//
//
//    @Column(name = "assignmentDate", nullable = true)
//    private LocalDateTime assignmentDate;
//
//    public LocalDateTime getAssignmentDate() {
//        return this.assignmentDate;
//    }
//
//    public void setAssignmentDate(LocalDateTime assignmentDate) {
//        this.assignmentDate = assignmentDate;
//    }
//
//
////    @Column(name = "status", length = 128, nullable = false)
////    private String status;
////
////    public String getStatus(){
////        return this.status;
////    }
////
////    public void setStatus(String status){
////        this.status = status;
////    }
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "status", nullable = false)
//    private Status status;
//
//    public Status getStatus(){
//        return this.status;
//    }
//
//    public void setStatus(Status status){
//        this.status = status;
//    }
//}
