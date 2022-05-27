package com.yoh.backend.service;

import com.yoh.backend.entity.Game;
import com.yoh.backend.entity.Organization;
import com.yoh.backend.entity.User;
import com.yoh.backend.repository.OrganizationRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    public void createOrganization(Organization organization) throws IllegalArgumentException{
        // TODO Добоавить валидацию и проверку на существование
        checkExistOrganization(organization);
        organizationRepository.createOrganization(organization);
    }

    public List<Organization> getAllOrganizations() {
        return organizationRepository.getAllOrganizations();
    }

    public List<Organization> getAllOrganizationsFilteredOrdered(String regex, String order) {
        if (!regex.equals(""))
            return organizationRepository.getAllOrganizationsOrdered(order)
                    .stream()
                    .filter(i -> i.getName().contains(regex))
                    .collect(Collectors.toList());
        return organizationRepository.getAllOrganizationsOrdered(order);
    }

    public void updateOrganization(Organization organization) throws IllegalArgumentException{
        organizationRepository.createOrganization(organization);
    }

    public void deleteOrganization(Organization organization) throws IllegalArgumentException{
        organizationRepository.deleteOrganization(organization);
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

    public Organization getOrganizationByName(String name) throws IllegalArgumentException{
        Organization organization = organizationRepository.getOrganizationByName(name);
        if (organization != null){
            return organization;
        }
        else throw new IllegalArgumentException(
                String.format("Sorry, but Organization with this name (%s) was not found.", name)
        );
    }

    public void checkExistOrganization(Organization organization) throws IllegalArgumentException{
        Organization organization_copy = organizationRepository.getOrganizationByName(organization.getName());
        if (organization_copy != null) {
            throw new IllegalArgumentException("Sorry, but Organization with this name is currently exist");
        }
    }
}
