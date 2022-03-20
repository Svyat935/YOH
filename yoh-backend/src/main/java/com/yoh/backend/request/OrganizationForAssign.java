package com.yoh.backend.request;

import javax.validation.constraints.NotNull;

public class OrganizationForAssign {

    @NotNull
    private String organization;

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {this.organization = organization;}
}
