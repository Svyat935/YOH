package com.yoh.backend.response;

import com.github.cliftonlabs.json_simple.JsonObject;

public class JSONResponse {
    private final Integer code;
    private final JsonObject jsonObject;

    public JSONResponse(Integer code, JsonObject jsonObject) {
        this.code = code;
        this.jsonObject = jsonObject;
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public Integer getCode() {
        return code;
    }

}
