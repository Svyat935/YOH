package com.yoh.backend.controller;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.yoh.backend.entity.*;
import com.yoh.backend.request.OrganizationForAssign;
import com.yoh.backend.request.RoleForAssign;
import com.yoh.backend.request.UserForAuthorize;
import com.yoh.backend.request.UserForCreatingRequest;
import com.yoh.backend.response.BaseResponse;
import com.yoh.backend.response.JSONResponse;
import com.yoh.backend.service.*;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private ResearcherService researcherService;

    @Autowired
    private TutorService tutorService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private SessionFactory sessionFactory;

    @GetMapping
    public BaseResponse testing() {
        return new BaseResponse("Test is successes.", 200);
    }

    @PostMapping("/registration")
    public JSONResponse createUser(@Valid @RequestBody UserForCreatingRequest userRequest) {
        User user = new User(userRequest.getLogin(), userRequest.getEmail(), userRequest.getPassword());
        try {
            this.userService.createUser(user);
            JsonObject response = new JsonObject();
            response.put("message", "User was created");
            return new JSONResponse(200, response);
        } catch (IllegalArgumentException e) {
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @PostMapping("/authorization")
    public JSONResponse authorizeUser(@Valid @RequestBody UserForAuthorize userRequest) {
        try {
            JsonObject response = new JsonObject();
            User user = this.userService.getUser(userRequest.getCredentials(), userRequest.getPassword());
            Integer role = user.getRole();
            String token = this.userService.generateToken(user.getId());
            response.put("token", token);
            response.put("role", role);
            String roleString = "NotAssigned";
            if (role != null){
                roleString = role == 0 ? "Admin" :
                        role == 1 ? "Patient" :
                                role == 2 ? "Researcher" : "Tutor";
            }
            response.put("roleString", roleString);
            return new JSONResponse(200, response);
        } catch (IllegalArgumentException e) {
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @PostMapping("/assign/role")
    public JSONResponse assignRoleUser(@RequestHeader("token") String token, @Valid @RequestBody RoleForAssign roleForAssign) {
        User userForAssign = this.userService.getUserById(this.userService.verifyToken(token));
        try {
            Integer role = roleForAssign.getRole();
            switch (role) {
                case 0 -> {
                    userForAssign.setRole(role);
                    this.userService.updateUser(userForAssign);
                    Admin admin = new Admin(userForAssign);
                    this.adminService.createAdmin(admin);
                    JsonObject response = new JsonObject();
                    response.put("message", "Admin was assigned");
                    return new JSONResponse(200, response);
                }
                case 1 -> {
                    userForAssign.setRole(role);
                    this.userService.updateUser(userForAssign);
                    Patient patient = new Patient(userForAssign);
                    this.patientService.createPatient(patient);
                    JsonObject response = new JsonObject();
                    response.put("message", "Patient was assigned");
                    return new JSONResponse(200, response);
                }
                case 2 -> {
                    userForAssign.setRole(role);
                    this.userService.updateUser(userForAssign);
                    Researcher researcher = new Researcher(userForAssign);
                    this.researcherService.createResearcher(researcher);
                    JsonObject response = new JsonObject();
                    response.put("message", "Researcher was assigned");
                    return new JSONResponse(200, response);
                }
                case 3 -> {
                    userForAssign.setRole(role);
                    this.userService.updateUser(userForAssign);
                    Tutor tutor = new Tutor(userForAssign);
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
            User userForAssign = this.userService.getUserById(this.userService.verifyToken(token));
            Organization newOrganization = this.organizationService.getOrganizationById(UUID.fromString(organizationForAssign.getOrganization()));

            Patient patient = patientService.getPatientByUser(userForAssign);
            if (patient == null) {
                Researcher researcher = researcherService.getResearcherByUser(userForAssign);
                if (researcher == null) {
                    Tutor tutor = tutorService.getTutorByUser(userForAssign);
                    if (tutor != null) {
                        tutor.setOrganization(newOrganization);
                    } else {
                        JsonObject exceptionResponse = new JsonObject();
                        exceptionResponse.put("message", String.format("User was not founded in roles %s", userForAssign.getId()));
                        return new JSONResponse(401, exceptionResponse);
                    }
                } else {
                    researcher.setOrganization(newOrganization);
                }
            } else {
                patient.setOrganization(newOrganization);
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
