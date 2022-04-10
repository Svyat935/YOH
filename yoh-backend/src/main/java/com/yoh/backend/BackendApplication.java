package com.yoh.backend;

import com.yoh.backend.entity.Admin;
import com.yoh.backend.entity.Organization;
import com.yoh.backend.entity.User;
import com.yoh.backend.repository.AdminRepository;
import com.yoh.backend.repository.OrganizationRepository;
import com.yoh.backend.repository.UserRepository;
import com.yoh.backend.service.AdminService;
import com.yoh.backend.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class BackendApplication {

    @Value("${INIT_ADMIN_LOGIN}")
    private String login;

    @Value("${INIT_ADMIN_PASSWORD}")
    private String password;

    @Value("${INIT_ADMIN_EMAIL}")
    private String email;

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(AdminService adminService, UserService userService, OrganizationRepository organizationRepository) {
        return (args) -> {
            User user = new User(this.login, this.email, this.password, 0);
            Admin admin = new Admin(user);
            // TODO: Delete organization later.
            Organization organization = new Organization(
                    "TestOrganization",
                    null,
                    null,
                    null,
                    null
            );
            organizationRepository.createOrganization(organization);

            try {
                userService.createUser(user);
                adminService.createAdmin(admin);
            } catch (IllegalArgumentException e) {
                System.out.println("Admin had been created.");
            }
        };
    }

}
