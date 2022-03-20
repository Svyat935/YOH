package com.yoh.backend.response;

import com.yoh.backend.entity.GameStatistic;

import javax.validation.constraints.NotNull;

public class GameStaticticResponse {

    private final String id;
    private final String patientID;
    private final String gameID;
    private final String type;
    private final String dateAction;
    private final String message;

    public GameStaticticResponse(GameStatistic gameStatistic) {
        this.id = gameStatistic.getId().toString();
        this.patientID = gameStatistic.getPatient().getId().toString();
        this.gameID = gameStatistic.getGame().getId().toString();
        this.type = gameStatistic.getType().toString();
        this.dateAction = gameStatistic.getDateAction().toString();
        this.message = gameStatistic.getMessage();
    }

    public String getId() { return id; }

    public String getPatientID() { return patientID; }

    public String getGameID() { return gameID; }

    public String getType() { return type; }

    public String getDateAction() { return dateAction; }

    public String getMessage() { return message; }

}
