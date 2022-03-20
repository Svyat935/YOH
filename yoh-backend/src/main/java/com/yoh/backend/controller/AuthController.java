package com.yoh.backend.controller;

import com.yoh.backend.entity.*;
import com.yoh.backend.request.OrganizationForAssign;
import com.yoh.backend.request.RoleForAssign;
import com.yoh.backend.request.UserForAuthorize;
import com.yoh.backend.request.UserForCreatingRequest;
import com.yoh.backend.response.BaseResponse;
import com.yoh.backend.response.TokenResponse;
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
    public BaseResponse testing(){
        return new BaseResponse("Test is successes.", 200);
    }

    @PostMapping("/registration")
    public BaseResponse createUser(@Valid @RequestBody UserForCreatingRequest userRequest) {
        User user = new User(userRequest.getLogin(), userRequest.getEmail(), userRequest.getPassword());
        try{
            this.userService.createUser(user);
        } catch (IllegalArgumentException e){
            return new BaseResponse(e.getMessage(), 401);
        }
        return new BaseResponse("User is created.", 200);
    }

    @PostMapping("/authorization")
    public TokenResponse authorizeUser(@Valid @RequestBody UserForAuthorize userRequest) {
        String token = this.userService.getUser(userRequest.getCredentials(), userRequest.getPassword());
        return new TokenResponse(token);
    }

    @PostMapping("/assign/role")
    public BaseResponse assignRoleUser(@RequestHeader("token") String token, @Valid @RequestBody RoleForAssign roleForAssign) throws IllegalArgumentException{
        User userForAssign = this.userService.getUserById(this.userService.verifyToken(token));
        try {
            switch (roleForAssign.getRole()) {
                case 0 -> {
                    Admin admin = new Admin(userForAssign);
                    this.adminService.createAdmin(admin);
                    return new BaseResponse("Admin was assigned", 200);
                }
                case 1 -> {
                    Patient patient = new Patient(userForAssign);
                    this.patientService.createPatient(patient);
                    return new BaseResponse("Patient was assigned", 200);
                }
                case 2 -> {
                    Researcher researcher = new Researcher(userForAssign);
                    this.researcherService.createResearcher(researcher);
                    return new BaseResponse("Researcher was assigned", 200);
                }
                case 3 -> {
                    Tutor tutor = new Tutor(userForAssign);
                    this.tutorService.createTutor(tutor);
                    return new BaseResponse("Tutor was assigned", 200);
                }
            }
        } catch (IllegalArgumentException e) {
            return new BaseResponse(e.getMessage(), 401);
        }
        return new BaseResponse(String.format("No such role: %s", roleForAssign.getRole()), 401);
    }

    @PostMapping("/assign/organization")
    public BaseResponse assignOrganization(@RequestHeader("token") String token, @Valid @RequestBody OrganizationForAssign organizationForAssign) throws IllegalArgumentException{
        User userForAssign = this.userService.getUserById(this.userService.verifyToken(token));
        Organization newOrganization = this.organizationService.getOrganizationById(UUID.fromString(organizationForAssign.getOrganization()));

        Patient patient = patientService.getPatientByUser(userForAssign);
        if (patient == null){
            Researcher researcher = researcherService.getResearcherByUser(userForAssign);
            if (researcher == null){
                Tutor tutor = tutorService.getTutorByUser(userForAssign);
                if (tutor != null) {
                    tutor.setOrganization(newOrganization);
                }
                else return new BaseResponse(String.format("User was not founded in roles %s", userForAssign.getId()), 401);
            }
            else {
                researcher.setOrganization(newOrganization);
            }
        }
        else {
            patient.setOrganization(newOrganization);
        }

        return new BaseResponse("Done", 200);

    }
}
