package com.yoh.backend.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import com.yoh.backend.enums.Gender;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "patients")
public class Patient implements Comparable<Patient>{
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
    private Date birthDate;

    public Date getBirthDate(){
        return this.birthDate;
    }

    public void setBirthDate(Date birthDate){
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


    @ManyToOne(fetch = FetchType.EAGER)
    private Tutor tutor;

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    //TODO Проверить
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    public Organization getOrganization(){
        return this.organization;
    }

    public void setOrganization(Organization organization){
        this.organization = organization;
        if (organization != null)
            this.setOrganizationString(organization.getName());
        else this.setOrganizationString(null);
    }

    @Column(name = "organizationString", length = 128, nullable = true)
    private String organizationString;

    public String getOrganizationString(){
        return this.organizationString;
    }

    public void setOrganizationString(String organizationString){
        this.organizationString = organizationString;
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


//    @ManyToMany
//    @LazyCollection(LazyCollectionOption.FALSE)
//    @JoinTable(name = "game_patient",
//            joinColumns = @JoinColumn(name = "game_id"),
//            inverseJoinColumns = @JoinColumn(name = "patient_id")
//    )
//    private List<Game> games;
//
//    public List<Game> getGames() {
//        return games;
//    }
//
//    public void setGames(List<Game> games) {
//        this.games = games;
//    }


//    @OneToMany(mappedBy = "patient")
//    @LazyCollection(LazyCollectionOption.FALSE)
//    private List<GameStatistic> gameStatistics;
//
//    public List<GameStatistic> getGameStatistics() {
//        return gameStatistics;
//    }
//
//    public void setGameStatistics(List<GameStatistic> gameStatistics){
//        this.gameStatistics = gameStatistics;
//    }


    @OneToMany(mappedBy = "patient")
    private List<TestStatistic> testStatistics;

    public List<TestStatistic> getTestStatistics() {
        return testStatistics;
    }

    public void setTestStatistics(List<TestStatistic> testStatistics){
        this.testStatistics = testStatistics;
    }


//    @OneToMany(mappedBy = "patient")
//    private List<GamePatient> gamePatientList;
//
//    public List<GamePatient> getGamePatientList() {
//        return gamePatientList;
//    }
//
//    public void setGamePatientList(List<GamePatient> gamePatientList) {
//        this.gamePatientList = gamePatientList;
//    }

    //    @OneToMany(mappedBy = "patient")
//    @LazyCollection(LazyCollectionOption.FALSE)
//    private List<GameStatus> gameStatuses;
//
//    public List<GameStatus> getGameStatuses() { return gameStatuses; }
//
//    public void setGameStatuses(List<GameStatus> gameStatuses) {
//        this.gameStatuses = gameStatuses;
//    }


    @OneToMany(mappedBy = "patient")
    private List<TestStatus> testStatuses;

    public List<TestStatus> getTestStatuses() { return testStatuses; }

    public void setTestStatuses(List<TestStatus> testStatuses) {
        this.testStatuses = testStatuses;
    }


    @Column(name = "image", unique = false, nullable = true)
    private String image;

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
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


    @Override
    public int compareTo(Patient o) {
        if (this.getName() == null && o.getName() == null)
            return 0;
        if (this.getName() == null)
            return -1;
        if (o.getName() == null)
            return 1;
        return this.getName().compareTo(o.getName());
    }
}
