package com.yoh.backend.entity;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;
import com.yoh.backend.enums.Gender;

@Entity
@Table(name = "patients")
public class Patient {
    public Patient() {}

    public Patient(User user){
        this.user = user;
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

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "surname", length = 128, nullable = true)
    private String surname;

    public String getSurname(){
        return this.surname;
    }

    public void setSurname(String surname){
        this.surname = surname;
    }

    @Column(name = "name", length = 128, nullable = true)
    private String name;

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    @Column(name = "secondName", length = 128, nullable = true)
    private String secondName;

    public String getSecondName(){
        return this.secondName;
    }

    public void setSecondName(String secondName){
        this.secondName = secondName;
    }

    @Column(name = "birthDate", length = 128, nullable = true)
    private String birthDate;

    public String getBirthDate(){
        return this.birthDate;
    }

    public void setBirthDate(String birthDate){
        this.birthDate = birthDate;
    }

    @Column(name = "NumberPhone", length = 128, nullable = true)
    private String numberPhone;

    public String getNumberPhone(){
        return this.numberPhone;
    }

    public void setNumberPhone(String numberPhone){
        this.numberPhone = numberPhone;
    }

    @Column(name = "address", length = 128, nullable = true)
    private String address;

    public String getAddress(){
        return this.address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    @Column(name = "gender")
    private Gender gender;

    public Gender getGender() { return this.gender; }

    public void setGender(Gender gender) { this.gender = gender; }


    @ManyToOne
    private Tutor tutor;

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    @OneToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    public Organization getOrganization(){
        return this.organization;
    }

    public void setOrganization(Organization organization){
        this.organization = organization;
    }


    @OneToMany(mappedBy = "patient")
    private List<Result> results;

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }


    @ManyToMany
    private List<Test> tests;

    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }


    @ManyToMany
    private List<Game> games;

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }


    @OneToMany(mappedBy = "patient")
    private List<GameStatistic> gameStatistics;

    public List<GameStatistic> getGameStatistics() {
        return gameStatistics;
    }

    public void setGameStatistics(List<GameStatistic> gameStatistics){
        this.gameStatistics = gameStatistics;
    }


    @OneToMany(mappedBy = "patient")
    private List<TestStatistic> testStatistics;

    public List<TestStatistic> getTestStatistics() {
        return testStatistics;
    }

    public void setTestStatistics(List<TestStatistic> testStatistics){
        this.testStatistics = testStatistics;
    }


    @OneToMany(mappedBy = "patient")
    private List<GameStatus> gameStatuses;

    public List<GameStatus> getGameStatuses() { return gameStatuses; }

    public void setGameStatuses(List<GameStatus> gameStatuses) {
        this.gameStatuses = gameStatuses;
    }


    @OneToMany(mappedBy = "patient")
    private List<TestStatus> testStatuses;

    public List<TestStatus> getTestStatuses() { return testStatuses; }

    public void setTestStatuses(List<TestStatus> testStatuses) {
        this.testStatuses = testStatuses;
    }


    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id + '\'' +
                ", user_id='" + user.getId() + '\'' +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", secondName='" + secondName + '\'' +
                '}';
    }


}