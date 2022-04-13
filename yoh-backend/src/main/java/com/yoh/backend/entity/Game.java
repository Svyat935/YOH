package com.yoh.backend.entity;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "games")
public class Game {
    public Game() {}

    public Game(String name, String description, String url) {
        this.name = name;
        this.description = description;
        this.url = url;
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


    @Column(name = "name", length = 128, nullable = false)
    private String name;

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }


    @Column(name = "description", length = 128, nullable = true)
    private String description;

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description){
        this.description = description;
    }


    @Column(name = "url", length = 128, nullable = false)
    private String url;

    public String getUrl(){
        return this.url;
    }

    public void setUrl(String url){
        this.url = url;
    }


    @ManyToMany
    private List<Patient> patients;

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }


    @OneToMany(mappedBy = "game")
    private List<GameStatistic> gameStatistics;

    public List<GameStatistic> getStatistics() {
        return gameStatistics;
    }

    public void setStatistics(List<GameStatistic> gameStatistics) {
        this.gameStatistics = gameStatistics;
    }
}
