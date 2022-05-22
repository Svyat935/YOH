package com.yoh.backend.request;

import com.github.cliftonlabs.json_simple.JsonObject;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

public class GameStartRequest {
    @NotNull
    private String date_start;

    public String getDate_start() {
        return date_start;
    }

    public void setDate_start(String date_start) {
        this.date_start = date_start;
    }

    @Nullable
    private String details;

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
