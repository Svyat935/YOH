package com.yoh.backend.service;

import com.yoh.backend.entity.Organization;
import com.yoh.backend.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    public void createOrganization(Organization organization) throws IllegalArgumentException{
        // TODO Добоавить валидацию и проверку на существование

        organizationRepository.createOrganization(organization);
    }

    public Organization getOrganizationById(UUID id) throws IllegalArgumentException{
        Organization organization = organizationRepository.getOrganizationByUUID(id);
        if (organization != null) {
            return organization;
        }
        else throw new IllegalArgumentException(
                String.format("Sorry, but Organization with this id (%s) was not found.", id)
        );
    }
}
