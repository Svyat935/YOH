package com.yoh.backend.request;

import com.github.cliftonlabs.json_simple.JsonObject;

import javax.validation.constraints.NotNull;
import java.util.List;

public class JsonRequest {
    @NotNull
    private JsonObject jsonObject;

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }
}
