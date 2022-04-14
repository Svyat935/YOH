package com.yoh.backend.response;

import com.yoh.backend.entity.GameStatus;

public class GameStatusResponse {

    private final String status;

    public GameStatusResponse(GameStatus gameStatus){
        this.status = gameStatus.getStatus();
    }

    public String getStatus() {
        return status;
    }

}
