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
}
