package com.yoh.backend.controller;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.yoh.backend.entity.Admin;
import com.yoh.backend.entity.Game;
import com.yoh.backend.request.AddGamesRequest;
import com.yoh.backend.request.EditPatientInfoRequest;
import com.yoh.backend.response.JSONResponse;
import com.yoh.backend.service.AdminService;
import com.yoh.backend.service.GameService;
import com.yoh.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private GameService gameService;

    @PostMapping(path = "/adding")
    public JSONResponse addGame(@RequestHeader("token") String token, @Valid @RequestBody AddGamesRequest addGamesRequest) {
        try{
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Game game = new Game(addGamesRequest.getName(), addGamesRequest.getDescription(), addGamesRequest.getUrl());
            gameService.createGame(game);
            JsonObject response = new JsonObject();
            response.put("message", "Game was added.");
            return new JSONResponse(200, response);
        }catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @GetMapping(path = "/all")
    public JSONResponse allGames(@RequestHeader("token") String token){
        try{
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            List<Game> games = this.gameService.getAllGames();
            JsonArray jsonArray = new JsonArray();
            if (games != null){
                for(Game game: games){
                    JsonObject response = new JsonObject();
                    response.put("id", game.getId());
                    response.put("name", game.getName());
                    response.put("description", game.getDescription());
                    response.put("url", game.getUrl());
                    jsonArray.add(response);
                }
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("games", jsonArray);
            return new JSONResponse(200, jsonObject);
        }catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }
}
