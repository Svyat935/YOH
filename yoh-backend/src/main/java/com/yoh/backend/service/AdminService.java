package com.yoh.backend.service;

import com.yoh.backend.entity.Admin;
import com.yoh.backend.entity.Game;
import com.yoh.backend.entity.Tutor;
import com.yoh.backend.entity.User;
import com.yoh.backend.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public void createAdmin(Admin admin) throws IllegalArgumentException{
        // TODO Добоавить валидацию и проверку на существование
        checksExistAdmin(admin);
        adminRepository.createAdmin(admin);
    }

    public void updateAdmin(Admin admin) throws IllegalArgumentException{
        adminRepository.createAdmin(admin);
    }

    public void deleteAdmin(Admin admin) throws IllegalArgumentException{
        adminRepository.deleteAdmin(admin);
    }

    public List<Admin> getAllAdmins(){
        return adminRepository.getAllAdmins();
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

    public Admin getAdminByUser(User user) throws IllegalArgumentException{
        Admin admin = adminRepository.getAdminByUser(user);
        if (admin != null) return admin;
        else throw new IllegalArgumentException(
                String.format("Sorry, but Admin with this user (%s) wasn't found.", user.getId().toString())
        );
    }

    private void checksExistAdmin(Admin admin) throws IllegalArgumentException{
        Admin admin_copy = adminRepository.getAdminByUser(admin.getUser());
        if (admin_copy != null){
            throw new IllegalArgumentException("Sorry, but Admin with this user was found.");
        }
    }


}
