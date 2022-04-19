package com.yoh.backend.request;

import com.github.cliftonlabs.json_simple.JsonObject;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

public class StatisticArray {
    @NotNull
    private ArrayList<JsonObject> records;

    public ArrayList<JsonObject> getRecords() {
        return records;
    }

    public void setGames_id(ArrayList<JsonObject> records) {this.records = records;}
}
