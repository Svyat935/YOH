package com.yoh.backend.request;

import javax.validation.constraints.NotNull;

public class StatisticToSend {
    @NotNull
    private Integer level;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }


    @NotNull
    private String level_name;

    public String getLevel_name() {
        return level_name;
    }

    public void setLevel_name(String level_name) {
        this.level_name = level_name;
    }


    @NotNull
    private String date_start;

    public String getDate_start() {
        return date_start;
    }

    public void setDate_start(String date_start) {
        this.date_start = date_start;
    }


    @NotNull
    private String date_end;

    public String getDate_end() {
        return date_end;
    }

    public void setDate_end(String date_end) {
        this.date_end = date_end;
    }


    @NotNull
    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


    @NotNull
    private Integer clicks;

    public Integer getClicks() {
        return clicks;
    }

    public void setClicks(Integer clicks) {
        this.clicks = clicks;
    }


    @NotNull
    private Integer missclicks;

    public Integer getMissclicks() {
        return missclicks;
    }

    public void setMissclicks(Integer missclicks) {
        this.missclicks = missclicks;
    }


    @NotNull
    private String details;

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @NotNull
    private String additional_fields;

    public String getAdditional_fields() {
        return additional_fields;
    }

    public void setAdditional_fields(String additional_fields) {
        this.additional_fields = additional_fields;
    }
}
