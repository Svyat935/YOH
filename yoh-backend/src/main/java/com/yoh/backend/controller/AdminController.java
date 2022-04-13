package com.yoh.backend.controller;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.yoh.backend.entity.*;
import com.yoh.backend.request.OrganizationForAdding;
import com.yoh.backend.request.OrganizationForAssign;
import com.yoh.backend.request.RoleForAssign;
import com.yoh.backend.request.UserForCreatingRequest;
import com.yoh.backend.response.JSONResponse;
import com.yoh.backend.response.UserInfoResponse;
import com.yoh.backend.service.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admins")
public class AdminController {
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

    @PostMapping("/organizations/new")
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
