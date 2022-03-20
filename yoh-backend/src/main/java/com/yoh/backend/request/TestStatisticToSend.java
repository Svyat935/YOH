package com.yoh.backend.request;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class TestStatisticToSend {
    @NotNull
    private String test_id;

    public void setTest_id(String test_id) {
        this.test_id = test_id;
    }

    public String getTest_id() {
        return test_id;
    }

    @NotNull
    private Short type;

    public void setType(Short type) {
        this.type = type;
    }

    public Short getType() {
        return type;
    }

    @NotNull
    private Date dateAction;

    public void setDateAction(Date dateAction) {
        this.dateAction = dateAction;
    }

    public Date getDateAction() {
        return dateAction;
    }

    @NotNull
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
