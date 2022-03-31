package com.yoh.backend.service;

import com.yoh.backend.entity.Admin;
import com.yoh.backend.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public void createAdmin(Admin admin) throws IllegalArgumentException{
        // TODO Добоавить валидацию и проверку на существование

        adminRepository.createAdmin(admin);
    }

    public Admin getAdminById(UUID id) throws IllegalArgumentException{
        Admin admin = adminRepository.getAdminByUUID(id);
        if (admin != null) {
            return admin;
        }
        else throw new IllegalArgumentException(
                String.format("Sorry, but Admin with this id (%s) wasn't found.", id)
        );
    }
}