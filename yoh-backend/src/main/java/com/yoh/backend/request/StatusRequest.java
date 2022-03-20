package com.yoh.backend.request;

import javax.validation.constraints.NotNull;

public class StatusRequest {
    @NotNull
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
