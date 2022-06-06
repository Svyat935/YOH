package com.yoh.backend.controller;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.yoh.backend.entity.*;
import com.yoh.backend.request.OrganizationForAssign;
import com.yoh.backend.request.RoleForAssign;
import com.yoh.backend.request.UserForAuthorize;
import com.yoh.backend.request.UserForCreatingRequest;
import com.yoh.backend.response.BaseResponse;
//import com.yoh.backend.response.JsonObject;
import com.yoh.backend.service.*;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private SessionFactory sessionFactory;

    @GetMapping
    public ResponseEntity<JsonObject> testing() {
        JsonObject response = new JsonObject();
        response.put("previous", false);
        response.put("next", false);
        response.put("count", 0);
        response.put("size", 0);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @PostMapping("/registration")
    public JsonObject createUser(@Valid @RequestBody UserForCreatingRequest userRequest) {
        User user = new User(userRequest.getLogin(), userRequest.getEmail(), userRequest.getPassword(), LocalDateTime.now(), 4);
        try {
            this.userService.createUser(user);
            JsonObject response = new JsonObject();
            response.put("message", "User was created");
            return response;
        } catch (IllegalArgumentException e) {
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return exceptionResponse;
        }
    }

    @PostMapping("/authorization")
    public JsonObject authorizeUser(@Valid @RequestBody UserForAuthorize userRequest) {
        try {
            JsonObject response = new JsonObject();
            User user = this.userService.getUser(userRequest.getCredentials(), userRequest.getPassword());
            Integer role = user.getRole();
            String token = this.userService.generateToken(user.getId());
            response.put("token", token);
            response.put("role", role);
            //TODO опитимизация
            String roleString = "NotAssigned";
            if (role != null){
                roleString = role == 0 ? "Admin" :
                        role == 1 ? "Patient" :
                                role == 2 ? "Researcher" :
                                        role == 3 ? "Tutor" : "Not assigned";
            }
            response.put("roleString", roleString);
            return response;
        } catch (IllegalArgumentException e) {
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return exceptionResponse;
        }
    }
}
