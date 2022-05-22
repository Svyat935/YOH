package com.yoh.backend.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "startedGames")
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class)
        ,
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class StartedGame {
    public StartedGame() {}

    public StartedGame(GamePatient gamePatient, LocalDateTime dateStart, LocalDateTime dateEnd, String details){
        this.gamePatient = gamePatient;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
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
    private GamePatient gamePatient;

    public GamePatient getGamePatient() {
        return gamePatient;
    }

    public void setGamePatient(GamePatient gamePatient) {
        this.gamePatient = gamePatient;
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


    @Type(type = "jsonb")
    @Column(name = "details", columnDefinition = "jsonb", nullable = true)
    private String details;

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
