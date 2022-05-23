package com.yoh.backend.entity;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "games")
public class Game {
    public Game() {}

    public Game(UUID id, String name, String type, String description, String url, LocalDateTime dateAdding) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.url = url;
        this.dateAdding = dateAdding;
    }

    public Game(String name, String type, String description, String url, LocalDateTime dateAdding) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.url = url;
        this.dateAdding = dateAdding;
    }

    @Id
//    @GeneratedValue
    private UUID id;

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    @Column(name = "name", length = 128, nullable = false, unique = true)
    private String name;

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }


    @Column(name = "type", length = 128, nullable = false)
    private String type;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Column(name = "description", length = 128, nullable = false)
    private String description;

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description){
        this.description = description;
    }


    @Column(name = "url", length = 128, nullable = true)
    private String url;

    public String getUrl(){
        return this.url;
    }

    public void setUrl(String url){
        this.url = url;
    }


    @Column(name = "dateAdding", nullable = false)
    private LocalDateTime dateAdding;

    public LocalDateTime getDateAdding() {
        return this.dateAdding;
    }

    public void setDateAdding(LocalDateTime dateAdding) {
        this.dateAdding = dateAdding;
    }


    @Column(name = "image", unique = false, nullable = true)
    private String image;

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }


//    @OneToMany(mappedBy = "game")
//    private List<GamePatient> gamePatientList;
//
//    public List<GamePatient> getGamePatientList() {
//        return gamePatientList;
//    }
//
//    public void setGamePatientList(List<GamePatient> gamePatientList) {
//        this.gamePatientList = gamePatientList;
//    }


    //    //TODO переименовать
//    @ManyToMany(mappedBy="games")
//    @LazyCollection(LazyCollectionOption.FALSE)
//    private List<Patient> patient;
//
//    public List<Patient> getPatient() {
//        return patient;
//    }
//
//    public void setPatients(List<Patient> patient) {
//        this.patient = patient;
//    }


//    @OneToMany(mappedBy = "game")
//    private List<GameStatistic> gameStatistics;
//
//    public List<GameStatistic> getStatistics() {
//        return gameStatistics;
//    }
//
//    public void setStatistics(List<GameStatistic> gameStatistics) {
//        this.gameStatistics = gameStatistics;
//    }
}
