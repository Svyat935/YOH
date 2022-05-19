package com.yoh.backend.response;

import com.yoh.backend.entity.Tutor;

import javax.validation.constraints.NotNull;

public class TutorInfoResponse {

    private final String id;
    private final String name;
    private final String surname;
    private final String secondName;
    private final String organization;
    private final String organizationString;

    public TutorInfoResponse(Tutor tutor) {
        this.id = tutor.getId().toString();
        this.name = tutor.getName();
        this.surname = tutor.getSurname();
        this.secondName = tutor.getSecondName();
        this.organization = tutor.getOrganization().getId().toString();
        this.organizationString = tutor.getOrganization().getName();
    }

    public String getId() { return id; }

    public String getName() { return name; }

    public String getSurname() { return surname; }

    public String getSecondName() { return secondName; }

    public String getOrganization() { return organization; }

    public String getOrganizationString() { return organizationString; }
}
