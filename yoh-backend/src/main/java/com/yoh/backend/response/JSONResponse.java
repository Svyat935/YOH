package com.yoh.backend.response;

import com.github.cliftonlabs.json_simple.JsonObject;

public class JSONResponse {
    private final Integer code;
    private final JsonObject jsonObject;

    public JSONResponse(JsonObject jsonObject, Integer code) {
        this.jsonObject = jsonObject;
        this.code = code;
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public Integer getCode() {
        return code;
    }

}
