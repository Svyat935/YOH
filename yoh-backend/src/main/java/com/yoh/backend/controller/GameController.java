package com.yoh.backend.controller;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.yoh.backend.entity.Admin;
import com.yoh.backend.entity.Game;
import com.yoh.backend.entity.User;
import com.yoh.backend.request.AddGamesRequest;
import com.yoh.backend.request.EditGameRequest;
import com.yoh.backend.request.EditPatientInfoRequest;
import com.yoh.backend.request.GameToRemove;
import com.yoh.backend.response.JSONResponse;
import com.yoh.backend.service.AdminService;
import com.yoh.backend.service.GameService;
import com.yoh.backend.service.UserService;
import com.yoh.backend.util.ImageUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private GameService gameService;

//    @PostMapping(path = "/adding")
//    public JSONResponse addGame(@RequestHeader("token") String token, @Valid @RequestBody AddGamesRequest addGamesRequest) {
//        try{
//            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
//            Game game = new Game(addGamesRequest.getName(), addGamesRequest.getDescription(), addGamesRequest.getUrl());
//            gameService.createGame(game);
//            JsonObject response = new JsonObject();
//            response.put("message", "Game was added.");
//            return new JSONResponse(200, response);
//        }catch (IllegalArgumentException e){
//            JsonObject exceptionResponse = new JsonObject();
//            exceptionResponse.put("message", e.getMessage());
//            return new JSONResponse(401, exceptionResponse);
//        }
//    }

    @GetMapping(path = "/all")
    public JSONResponse allGames(@RequestHeader("token") String token,
                                 @RequestParam(value = "regex", required = false, defaultValue = "") String regex,
                                 @RequestParam(value = "typeRegex", required = false, defaultValue = "") String typeRegex) {
        try{
            User user = this.userService.getUserById(this.userService.verifyToken(token));
            List<Game> games = this.gameService.getAllGamesFiltered(regex, typeRegex);
            JsonArray jsonArray = new JsonArray();
            if (games != null){
                for(Game game: games){
                    JsonObject response = new JsonObject();
                    response.put("id", game.getId());
                    response.put("name", game.getName());
                    response.put("type", game.getType());
                    response.put("description", game.getDescription());
                    response.put("url", game.getUrl());
                    if (game.getImage() != null) response.put("image", ImageUtility.decompressImage(game.getImage()));
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

    @GetMapping(path = "/get")
    public JSONResponse getGame(@RequestHeader("token") String token,
                                @RequestParam String gameID) {
        try{
            User user = this.userService.getUserById(this.userService.verifyToken(token));
            Game game = this.gameService.getGameById(UUID.fromString(gameID));
            JsonObject response = new JsonObject();
            response.put("id", game.getId());
            response.put("name", game.getName());
            response.put("type", game.getType());
            response.put("description", game.getDescription());
            response.put("url", game.getUrl());
            if (game.getImage() != null) response.put("image", ImageUtility.decompressImage(game.getImage()));
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("gameInfo", response);
            return new JSONResponse(200, jsonObject);
        }catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @DeleteMapping(path = "/removing")
    public JSONResponse removeGame(@RequestHeader("token") String token,
                                   @Valid @RequestBody GameToRemove gameToRemove) {
        try {
            //TODO Отцепление от пациентов, проверить возможно работает
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Game game = this.gameService.getGameById(UUID.fromString(gameToRemove.getGame_id()));
            gameService.deleteGame(game);
            JsonObject response = new JsonObject();
            response.put("messag", "Game was deleted");
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e) {
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @PutMapping(path = "/changing")
    public JSONResponse changeGame(@RequestHeader("token") String token,
                                   @Valid @RequestBody EditGameRequest editGameRequest) {
        try {
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Game game = this.gameService.getGameById(UUID.fromString(editGameRequest.getGame_id()));
            if (editGameRequest.getName() != null)
                game.setName(editGameRequest.getName());
            if (editGameRequest.getDescription() != null)
                game.setDescription(editGameRequest.getDescription());
            if (editGameRequest.getUrl() != null)
                game.setUrl(editGameRequest.getUrl());
            gameService.updateGame(game);
            JsonObject response = new JsonObject();
            response.put("status", "OK");
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e) {
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }
}
