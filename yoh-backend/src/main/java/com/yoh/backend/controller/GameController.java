package com.yoh.backend.controller;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.yoh.backend.entity.Admin;
import com.yoh.backend.entity.Game;
import com.yoh.backend.entity.Patient;
import com.yoh.backend.entity.User;
import com.yoh.backend.request.AddGamesRequest;
import com.yoh.backend.request.EditGameRequest;
import com.yoh.backend.request.EditPatientInfoRequest;
import com.yoh.backend.request.GameToRemove;
import com.yoh.backend.response.JSONResponse;
import com.yoh.backend.service.*;
import com.yoh.backend.util.ImageUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private GameService gameService;

    @Autowired
    private GamePatientService gamePatientService;

    @Autowired
    private PatientService patientService;

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
                                 @RequestParam(value = "typeRegex", required = false, defaultValue = "") String typeRegex,
                                 @RequestParam(value = "limit", required = true) Integer limit,
                                 @RequestParam(value = "start", required = true) Integer start,
                                 @RequestParam(value = "order", required = false, defaultValue = "1") String order,
                                 @RequestParam(value = "patientID", required = false) String patientID) {
        try{
            User user = this.userService.getUserById(this.userService.verifyToken(token));
            List<Game> gameList = this.gameService.getAllGamesFiltered(typeRegex, order);
            JsonObject response = new JsonObject();
            if (!regex.equals("")){
                gameList = gameList.stream().filter(i -> i.getName().toLowerCase().contains(regex.toLowerCase()))
                        .collect(Collectors.toList());
            }
            gameList.forEach(i -> System.out.println(i.getId().toString()));
            if (patientID != null) {
                List<Game> patientGames = this.gamePatientService.getAllGamesByPatient(this.patientService.getPatientById(UUID.fromString(patientID)));
//                gameList = gameList.stream().filter(i -> !patientGames.contains(i)).collect(Collectors.toList());
                for (Game gamepat : patientGames){
                    if (gameList.remove(gamepat)) {
                        System.out.println("Game was cleared");
                    }
                    else System.out.println("ssdsd");
                    System.out.println(gamepat.getId().toString());
                }
            }
            if (gameList.size() == 0) {
//                JsonObject response = new JsonObject();
                response.put("previous", false);
                response.put("next", false);
                response.put("count", 0);
                response.put("results", new ArrayList<>());
                return new JSONResponse(200, response);
            }
            ArrayList<JsonObject> gamesList = new ArrayList<JsonObject>();
//            JsonObject response = new JsonObject();



            //Pagination
            if (start >= gameList.size())
                throw new IllegalArgumentException(
                        String.format("No element at that index (%s)", start)
                );
            int lastIndex;
            if (start + limit > gameList.size()){
                lastIndex = gameList.size();
                response.put("next", false);
            }
            else {
                lastIndex = start + limit;
                response.put("next", true);
            }
            if (start == 0) response.put("previous", false);
            else response.put("previous", true);
            List<Game> paginatedGameList = new ArrayList<>();
            for (int i = start; i < lastIndex; i++){
                paginatedGameList.add(gameList.get(i));
            }
            response.put("count", paginatedGameList.size());


//            if (games.size() != 0){
            for(Game game: paginatedGameList){
                JsonObject gameInfo = new JsonObject();
                gameInfo.put("id", game.getId());
                gameInfo.put("name", game.getName());
                gameInfo.put("type", game.getType());
                gameInfo.put("description", game.getDescription());
                gameInfo.put("url", game.getUrl());
                gameInfo.put("image", game.getImage());
                gameInfo.put("addAdding", game.getDateAdding());
                gamesList.add(gameInfo);
            }
//            }

            response.put("results", gamesList);
            return new JSONResponse(200, response);
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
            response.put("image", game.getImage());
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
