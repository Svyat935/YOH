package com.yoh.backend.request;

import com.github.cliftonlabs.json_simple.JsonObject;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class StatisticArray {
    @NotNull
    private List<JsonObject> records;

    public List<JsonObject> getRecords() {
        return records;
    }

    public void setGames_id(List<JsonObject> records) {this.records = records;}
}
