package com.yoh.backend.request;

import com.github.cliftonlabs.json_simple.JsonObject;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

public class GameEndRequest {
    @NotNull
    private String date_end;

    public String getDate_end() {
        return date_end;
    }

    public void setDate_end(String date_end) {
        this.date_end = date_end;
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
