package com.yoh.backend.response;

import com.yoh.backend.entity.GameStatistic;
import com.yoh.backend.entity.TestStatistic;

import javax.validation.constraints.NotNull;

public class TestStatisticResponse {

    private final String id;
    private final String patientID;
    private final String testID;
    private final String type;
    private final String dateAction;
    private final String message;

    public TestStatisticResponse(TestStatistic testStatistic) {
        this.id = testStatistic.getId().toString();
        this.patientID = testStatistic.getPatient().getId().toString();
        this.testID = testStatistic.getTest().getId().toString();
        this.type = testStatistic.getType().toString();
        this.dateAction = testStatistic.getDateAction().toString();
        this.message = testStatistic.getMessage();
    }

    public String getId() { return id; }

    public String getPatientID() { return patientID; }

    public String getTestID() { return testID; }

    public String getType() { return type; }

    public String getDateAction() { return dateAction; }

    public String getMessage() { return message; }
}
