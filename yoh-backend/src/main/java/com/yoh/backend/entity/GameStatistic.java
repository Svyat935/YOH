package com.yoh.backend.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import io.swagger.models.auth.In;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.json.bind.Jsonb;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "gameStatistics")
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class)
        ,
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class GameStatistic {
    public GameStatistic() {}

    public GameStatistic(StartedGame startedGame,
                         Integer level,
                         String levelName,
                         LocalDateTime dateStart,
                         LocalDateTime dateEnd,
                         Integer type,
                         Integer clicks,
                         Integer missClicks,
                         String details) {
        this.startedGame = startedGame;
        this.level = level;
        this.levelName = levelName;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.type = type;
        this.clicks = clicks;
        this.missClicks = missClicks;
        this.details = details;
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
    private StartedGame startedGame;

    public StartedGame getStartedGame() {
        return startedGame;
    }

    public void setStartedGame(StartedGame startedGame) {
        this.startedGame = startedGame;
    }


    @Column(name = "level", nullable = false)
    private Integer level;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }


    @Column(name = "levelName", nullable = false)
    private String levelName;

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }


    @Column(name = "dateStart", nullable = false)
    private LocalDateTime dateStart;

    public LocalDateTime getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDateTime dateStart) {
        this.dateStart = dateStart;
    }


    @Column(name = "dateEnd", nullable = true)
    private LocalDateTime dateEnd;

    public LocalDateTime getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDateTime dateEnd) {
        this.dateEnd = dateEnd;
    }


    @Column(name = "type", nullable = false)
    private Integer type;

    public Integer getType(){
        return this.type;
    }

    public void setType(Integer type){
        this.type = type;
    }


    @Column(name = "clicks", nullable = true)
    private Integer clicks;

    public Integer getClicks() {
        return clicks;
    }

    public void setClicks(Integer clicks) {
        this.clicks = clicks;
    }


    @Column(name = "missClicks", nullable = true)
    private Integer missClicks;

    public Integer getMissClicks() {
        return missClicks;
    }

    public void setMissClicks(Integer missClicks) {
        this.missClicks = missClicks;
    }


    @Type(type = "jsonb")
    @Column(name = "details", columnDefinition = "jsonb", nullable = true)
    private String details;

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    //    public GameStatistic() {}
//
//    public GameStatistic(GamePatient gamePatient, Short type, LocalDateTime dateAction, Short answerNumber, LocalDateTime dateStart, Integer clicks, Integer missClicks, String details) {
//        this.gamePatient = gamePatient;
//        this.type = type;
//        this.dateAction = dateAction;
//        this.answerNumber = answerNumber;
//        this.dateStart = dateStart;
//        this.clicks = clicks;
//        this.missClicks = missClicks;
//        this.details = details;
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
//    @ManyToOne
//    private GamePatient gamePatient;
//
//    public GamePatient getGamePatient() {
//        return gamePatient;
//    }
//
//    public void setGamePatient(GamePatient gamePatient) {
//        this.gamePatient = gamePatient;
//    }
//
//
//    @Column(name = "type", nullable = false)
//    private Short type;
//
//    public Short getType(){
//        return this.type;
//    }
//
//    public void setType(Short type){
//        this.type = type;
//    }
//
//
//    @Column(name = "dateAction", nullable = false)
//    private LocalDateTime dateAction;
//
//    public LocalDateTime getDateAction(){
//        return this.dateAction;
//    }
//
//    public void setDateAction(LocalDateTime dateAction){
//        this.dateAction = dateAction;
//    }
//
//
//    @Column(name = "answerNumber", nullable = true)
//    private Short answerNumber;
//
//    public Short getAnswerNumber() {
//        return this.answerNumber;
//    }
//
//    public void setAnswerNumber(Short answerNumber) {
//        this.answerNumber = answerNumber;
//    }
//
//
//    @Column(name = "dateStart", nullable = true)
//    private LocalDateTime dateStart;
//
//    public LocalDateTime getDateStart() {
//        return dateStart;
//    }
//
//    public void setDateStart(LocalDateTime dateStart) {
//        this.dateStart = dateStart;
//    }
//
//
//    @Column(name = "clicks", nullable = true)
//    private Integer clicks;
//
//    public Integer getClicks() {
//        return clicks;
//    }
//
//    public void setClicks(Integer clicks) {
//        this.clicks = clicks;
//    }
//
//
//    @Column(name = "missClicks", nullable = true)
//    private Integer missClicks;
//
//    public Integer getMissClicks() {
//        return missClicks;
//    }
//
//    public void setMissClicks(Integer missClicks) {
//        this.missClicks = missClicks;
//    }
//
//
//    @Type(type = "jsonb")
//    @Column(name = "details", columnDefinition = "jsonb", nullable = true)
//    private String details;
//
//    public String getDetails() {
//        return details;
//    }
//
//    public void setDetails(String details) {
//        this.details = details;
//    }

}
