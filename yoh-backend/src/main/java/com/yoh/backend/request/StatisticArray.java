package com.yoh.backend.request;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

public class StatisticArray {
    @NotNull
    private ArrayList<GameStatisticToSend> records;

    public ArrayList<GameStatisticToSend> getRecords() {
        return records;
    }

    public void setGames_id(ArrayList<GameStatisticToSend> records) {this.records = records;}
}
