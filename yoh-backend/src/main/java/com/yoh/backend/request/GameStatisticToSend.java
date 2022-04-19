package com.yoh.backend.request;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class GameStatisticToSend {

    @NotNull
    private String DateAction;

    public void setDateAction(String DateAction) {
        this.DateAction = DateAction;
    }

    public String getDateAction() {
        return this.DateAction;
    }

    @NotNull
    private Short Type;

    public void setType(Short Type) {
        this.Type = Type;
    }

    public Short getType() {
        return Type;
    }

    @Nullable
    private String AnswerNumber;

    public void setAnswerNumber(Date dateAction) {
        this.AnswerNumber = AnswerNumber;
    }

    public String getAnswerNumber() {
        return AnswerNumber;
    }

}
