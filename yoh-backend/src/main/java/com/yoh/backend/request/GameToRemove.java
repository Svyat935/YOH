package com.yoh.backend.request;

import javax.validation.constraints.NotNull;

public class GameToRemove {
    @NotNull
    private String game_id;

    public void setGame_id(String game_id){
        this.game_id = game_id;
    }

    public String getGame_id() {
        return this.game_id;
    }
}
