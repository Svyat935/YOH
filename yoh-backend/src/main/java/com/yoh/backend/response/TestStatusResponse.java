package com.yoh.backend.response;

import com.yoh.backend.entity.TestStatus;

public class TestStatusResponse {

    private final String status;

    public TestStatusResponse(TestStatus testStatus){
        this.status = testStatus.getStatus();
    }

    public String getStatus() {
        return status;
    }

}
