package com.yoh.backend.request;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class GameStatisticToSend {

    @NotNull
    private String game_id;

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public String getGame_id() {
        return game_id;
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
