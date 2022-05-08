package com.yoh.backend.controller;

import antlr.StringUtils;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.yoh.backend.entity.*;
import com.yoh.backend.request.*;
import com.yoh.backend.response.JSONResponse;
import com.yoh.backend.response.UserInfoResponse;
import com.yoh.backend.service.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.*;

@RestController
@RequestMapping("/admins")
public class AdminController {
    @Value("${GAMES_FOLDER}")
    private String games_folder;

    @Value("${WRAPPER}")
    private String wrapper;

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private ResearcherService researcherService;

    @Autowired
    private TutorService tutorService;

    @Autowired
    private GameService gameService;

    @GetMapping(path = "/users/all")
    public JSONResponse getUsers(@RequestHeader("token") String token) {
        try {
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            JsonObject response = new JsonObject();
            List<UserInfoResponse> responseList = new ArrayList<>();
            for (User user: this.userService.getAllUsers()) {
                responseList.add(new UserInfoResponse(user));
            }
//            response.put("userList", this.userService.getAllUsers());
            response.put("userList", responseList);
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @PostMapping(path = "/upload/games")
    public JSONResponse uploadGames(@RequestHeader("token") String token, @RequestParam MultipartFile file) {
        try {
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            if (!file.isEmpty()) {
//                byte[] bytes = file.getBytes();
                System.out.println("************************ 1 line *****************************");
//                System.out.println(this.games_folder);

                String pathFile = this.games_folder + file.getOriginalFilename();
                System.out.println(pathFile);
                Path path = Paths.get(pathFile);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

////                System.out.println(21);
////                System.out.println(file.getName());
////                System.out.println(file.getOriginalFilename());
////                System.out.println(21);
////                String pathFile = this.games_folder + "/" + file.getOriginalFilename();
////                System.out.println(21);
////                Path path = Paths.get(pathFile);
////                System.out.println(21);
////                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
////                System.out.println(21);
//
////                String fileName = file.getOriginalFilename();
////                String location = this.games_folder;
////                File pathFile = new File(location);
////                if (!pathFile.exists()) {
////                    pathFile.mkdir();
////                }
////                pathFile = new File(location + fileName);
////                file.transferTo(pathFile);
//
////                BufferedOutputStream stream =
////                        new BufferedOutputStream(new FileOutputStream(new File(this.games_folder + "/" + file.getName())));
////                stream.write(bytes);
////                stream.close();
//                System.out.println("************************ 2 line *****************************");
////                try (ZipInputStream zin = new ZipInputStream(new FileInputStream(this.games_folder + "/" + file.getName()))){
//                try (ZipInputStream zin = new ZipInputStream(new FileInputStream(pathFile))){
//
//                    ZipEntry entry;
//                    String name;
////                    Long size;
//                    while ((entry = zin.getNextEntry()) != null) {
//                        name = entry.getName();
////                        size = entry.getSize();
//
//                        System.out.println("************************ 3 line *****************************");
//                        FileOutputStream fout = new FileOutputStream(this.games_folder + name);
//                        for (int c = zin.read(); c != -1; c = zin.read()) {
//                            fout.write(c);
//                        }
//                        fout.flush();
//                        zin.closeEntry();
//                        fout.close();
//                        Game game = new Game(name, null, this.wrapper + "/" + name);
//                        this.gameService.createGame(game);
//                    }
//                }
//                new File(this.games_folder + "/" + file.getName()).delete();
            }
            JsonObject response = new JsonObject();
            response.put("message", "games successfully uploaded");
            return new JSONResponse(200, response);
        }
        catch (Exception e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @GetMapping(path = "/organizations/all")
    public JSONResponse getOrganizations(@RequestHeader("token") String token) {
        try {
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            JsonObject response = new JsonObject();
            response.put("organizationList", this.organizationService.getAllOrganizations());
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @PostMapping("/organizations/add")
    public JSONResponse createOrganization(@RequestHeader("token") String token, @Valid @RequestBody OrganizationForAdding organizationForAdding) {
        try {
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Organization organization = new Organization(organizationForAdding.getName(), organizationForAdding.getAddress(), organizationForAdding.getPhone(),
                    organizationForAdding.getEmail(), organizationForAdding.getWebsite());
            this.organizationService.createOrganization(organization);
            JsonObject response = new JsonObject();
            response.put("message", "Organization was created");
            return new JSONResponse(200, response);
        } catch (IllegalArgumentException e) {
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @DeleteMapping("/organizations/delete")
    public JSONResponse deleteOrganization(@RequestHeader("token") String token, @Valid @RequestBody OrganizationToDelete organizationToDelete) {
        try {
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Organization organization = this.organizationService.getOrganizationById(UUID.fromString(organizationToDelete.getOrganization()));

            //TODO улучшить
            List<Researcher> researchers = this.researcherService.getAllResearchersByOrganization(organization);
            for (Researcher researcher: researchers){
                researcher.setOrganization(null);
                this.researcherService.updateResearcher(researcher);
            }
            List<Tutor> tutors = this.tutorService.getAllTutorsByOrganization(organization);
            for (Tutor tutor: tutors) {
                tutor.setOrganization(null);
                this.tutorService.updateTutor(tutor);
            }
            List<Patient> patients = this.patientService.getAllPatientsByOrganization(organization);
            for (Patient patient: patients) {
                patient.setOrganization(null);
                this.patientService.updatePatient(patient);
            }

            this.organizationService.deleteOrganization(organization);
            JsonObject response = new JsonObject();
            response.put("message", "Organization was deleted");
            return new JSONResponse(200, response);

        } catch (IllegalArgumentException e) {
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @PostMapping("/assign/role")
    public JSONResponse assignRoleUser(@RequestHeader("token") String token, @Valid @RequestBody RoleForAssign roleForAssign) {
        try {
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            User userForAssign = this.userService.getUserById(UUID.fromString(roleForAssign.getUser()));
            //TODO: Delete organization
            Organization testOrganization = this.organizationService.getOrganizationByName("TestOrganization");
            Integer role = roleForAssign.getRole();
            switch (role) {
                case 0 -> {
                    userForAssign.setRole(role);
                    this.userService.updateUser(userForAssign);
                    Admin user_admin = new Admin(userForAssign);
                    this.adminService.createAdmin(user_admin);
                    JsonObject response = new JsonObject();
                    response.put("message", "Admin was assigned");
                    return new JSONResponse(200, response);
                }
                case 1 -> {
                    userForAssign.setRole(role);
                    this.userService.updateUser(userForAssign);
                    Patient patient = new Patient(userForAssign);
                    //TODO: Delete Organization later.
                    patient.setOrganization(testOrganization);
                    this.patientService.createPatient(patient);
                    JsonObject response = new JsonObject();
                    response.put("message", "Patient was assigned");
                    return new JSONResponse(200, response);
                }
                case 2 -> {
                    userForAssign.setRole(role);
                    this.userService.updateUser(userForAssign);
                    Researcher researcher = new Researcher(userForAssign);
                    //TODO: Delete Organization later.
                    researcher.setOrganization(testOrganization);
                    this.researcherService.createResearcher(researcher);
                    JsonObject response = new JsonObject();
                    response.put("message", "Researcher was assigned");
                    return new JSONResponse(200, response);
                }
                case 3 -> {
                    userForAssign.setRole(role);
                    this.userService.updateUser(userForAssign);
                    Tutor tutor = new Tutor(userForAssign);
                    //TODO: Delete Organization later.
                    tutor.setOrganization(testOrganization);
                    this.tutorService.createTutor(tutor);
                    JsonObject response = new JsonObject();
                    response.put("message", "Tutor was assigned");
                    return new JSONResponse(200, response);
                }
            }
        } catch (IllegalArgumentException e) {
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
        JsonObject defaultResponse = new JsonObject();
        defaultResponse.put("message", "No such role");
        return new JSONResponse(401, defaultResponse);
    }

    @PostMapping("/assign/organization")
    public JSONResponse assignOrganization(@RequestHeader("token") String token, @Valid @RequestBody OrganizationForAssign organizationForAssign) {
        try {
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            User userForAssign = this.userService.getUserById(UUID.fromString(organizationForAssign.getUser()));
            Organization newOrganization = this.organizationService.getOrganizationById(UUID.fromString(organizationForAssign.getOrganization()));

            Patient patient = patientService.getPatientByUser(userForAssign);
            if (patient == null) {
                Researcher researcher = researcherService.getResearcherByUser(userForAssign);
                if (researcher == null) {
                    Tutor tutor = tutorService.getTutorByUser(userForAssign);
                    if (tutor != null) {
                        tutor.setOrganization(newOrganization);
                        this.tutorService.updateTutor(tutor);
                    } else {
                        JsonObject exceptionResponse = new JsonObject();
                        exceptionResponse.put("message", String.format("User was not founded in roles %s", userForAssign.getId()));
                        return new JSONResponse(401, exceptionResponse);
                    }
                } else {
                    researcher.setOrganization(newOrganization);
                    this.researcherService.updateResearcher(researcher);
                }
            } else {
                patient.setOrganization(newOrganization);
                this.patientService.updatePatient(patient);
            }
            JsonObject response = new JsonObject();
            response.put("message", "Organization was assigned");
            return new JSONResponse(200, response);
        } catch (IllegalArgumentException e) {
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }

    }
}
