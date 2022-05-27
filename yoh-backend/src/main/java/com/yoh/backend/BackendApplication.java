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
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;


@SpringBootApplication
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class BackendApplication {

    @Value("${INIT_ADMIN_LOGIN}")
    private String login;

    @Value("${INIT_ADMIN_PASSWORD}")
    private String password;

    @Value("${INIT_ADMIN_EMAIL}")
    private String email;

    @Value("${GAMES_FOLDER}")
    private String games_folder;

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
                    null,
                    LocalDateTime.now()
            );
            try {
                userService.createUser(user);
                adminService.createAdmin(admin);
                organizationRepository.createOrganization(organization);
            } catch (IllegalArgumentException e) {
                System.out.println("Admin had been created.");
            }
        };
    }

    @Bean
    public void create_folder() {
        try {
            String[] games_folders = this.games_folder.split("/");
//        new File(games_folders[games_folders.length - 1]).mkdir();
            Files.createDirectories(Paths.get(games_folders[games_folders.length - 1]));
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        return factory.createMultipartConfig();
    }

}
