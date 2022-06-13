package com.yoh.backend.request;

import com.github.cliftonlabs.json_simple.JsonObject;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.List;

public class JsonRequest {
    @NotNull
    private String details;

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
