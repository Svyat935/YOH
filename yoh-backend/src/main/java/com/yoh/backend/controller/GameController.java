package com.yoh.backend.controller;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.yoh.backend.entity.*;
import com.yoh.backend.enums.GamePatientStatus;
import com.yoh.backend.enums.GameActiveStatus;
import com.yoh.backend.request.EditGameRequest;
import com.yoh.backend.request.GameToRemove;
//import com.yoh.backend.response.JsonObject;
import com.yoh.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.util.ArrayList;
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

    @Autowired
    private GamePatientService gamePatientService;

    @Autowired
    private PatientService patientService;

//    @PostMapping(path = "/adding")
//    public JsonObject addGame(@RequestHeader("token") String token, @Valid @RequestBody AddGamesRequest addGamesRequest) {
//        try{
//            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
//            Game game = new Game(addGamesRequest.getName(), addGamesRequest.getDescription(), addGamesRequest.getUrl());
//            gameService.createGame(game);
//            JsonObject response = new JsonObject();
//            response.put("message", "Game was added.");
//            return response);
//        }catch (IllegalArgumentException e){
//            JsonObject exceptionResponse = new JsonObject();
//            exceptionResponse.put("message", e.getMessage());
//            return exceptionResponse);
//        }
//    }

    @GetMapping(path = "/all")
    public JsonObject allGames(@RequestHeader("token") String token,
                                 @RequestParam(value = "regex", required = false, defaultValue = "") String regex,
                                 @RequestParam(value = "typeRegex", required = false, defaultValue = "") String typeRegex,
                                 @RequestParam(value = "limit", required = true) Integer limit,
                                 @RequestParam(value = "start", required = true) Integer start,
                                 @RequestParam(value = "order", required = false, defaultValue = "1") String order,
                                 @RequestParam(value = "patientID", required = false) String patientID) {
        //TODO Переделать пагинацию
        try{
            User user = this.userService.getUserById(this.userService.verifyToken(token));
            if (user.getRole() == 1 || user.getRole() == 2)
                throw new IllegalArgumentException("This user doesn't have permission");
            JsonObject response = new JsonObject();
            ArrayList<UUID> UUIDList = new ArrayList<>();
            if (patientID != null) this.gamePatientService.getAllGamesByPatient(this.patientService.getPatientById(UUID.fromString(patientID))).forEach(i -> UUIDList.add(i.getId()));
            int listCount = this.gameService.getAllActiveGamesFilteredCount(typeRegex, regex, UUIDList);
            if (listCount == 0) {
                response.put("previous", false);
                response.put("next", false);
                response.put("count", 0);
                response.put("size", 0);
                response.put("results", new ArrayList<>());
                return response;
            }
            if (start >= listCount)
                throw new IllegalArgumentException(String.format("No element at that index (%s)", start));

            List<Game> gameList = this.gameService.getAllActiveGamesFiltered(order, typeRegex, regex, UUIDList, start, limit);
            if (start == 0)
                response.put("previous", false);
            else response.put("previous", true);

            if (start + limit > listCount)
                response.put("next", false);
            else response.put("next", true);

            response.put("count", gameList.size());
            response.put("size", listCount);
//            ArrayList<JsonObject> gamesInfoList = new ArrayList<JsonObject>();
//            for(Game game: gameList){
//                JsonObject gameInfo = new JsonObject();
//                gameInfo.put("id", game.getId());
//                gameInfo.put("name", game.getName());
//                gameInfo.put("type", game.getType());
//                gameInfo.put("description", game.getDescription());
//                gameInfo.put("url", game.getUrl());
//                gameInfo.put("image", game.getImage());
//                gameInfo.put("addAdding", game.getDateAdding());
//                gameInfo.put("useStatistics", game.getUseStatistic());
//                gameInfo.put("gameStatus", game.getGameStatus());
//                gamesInfoList.add(gameInfo);
//            }
////            }
//
//            response.put("results", gamesInfoList);
            response.put("results", gameList);
            return response;

//            User user = this.userService.getUserById(this.userService.verifyToken(token));
//            List<Game> gameList = this.gameService.getAllGamesFiltered(typeRegex, order, regex);
//            JsonObject response = new JsonObject();
//            ArrayList<UUID> sdasds = new ArrayList<>();
////            if (!regex.equals("")){
////                gameList = gameList.stream().filter(i -> i.getName().toLowerCase().contains(regex.toLowerCase()))
////                        .collect(Collectors.toList());
////            }
//            if (patientID != null) {
//                this.gamePatientService.getAllGamesByPatient(this.patientService.getPatientById(UUID.fromString(patientID))).forEach(i -> sdasds.add(i.getId()));
////                gameList = gameList.stream().filter(i -> !patientGames.contains(i)).collect(Collectors.toList());
//                gameList = gameList.stream().filter(i -> !sdasds.contains(i.getId())).collect(Collectors.toList());
//            }
//            if (gameList.size() == 0) {
////                JsonObject response = new JsonObject();
//                response.put("previous", false);
//                response.put("next", false);
//                response.put("count", 0);
//                response.put("size", 0);
//                response.put("results", new ArrayList<>());
//                return response);
//            }
//            ArrayList<JsonObject> gamesList = new ArrayList<JsonObject>();
////            JsonObject response = new JsonObject();
//
//
//
//            //Pagination
//            if (start >= gameList.size())
//                throw new IllegalArgumentException(
//                        String.format("No element at that index (%s)", start)
//                );
//            int lastIndex;
//            if (start + limit > gameList.size()){
//                lastIndex = gameList.size();
//                response.put("next", false);
//            }
//            else {
//                lastIndex = start + limit;
//                response.put("next", true);
//            }
//            if (start == 0) response.put("previous", false);
//            else response.put("previous", true);
//            List<Game> paginatedGameList = new ArrayList<>();
//            for (int i = start; i < lastIndex; i++){
//                paginatedGameList.add(gameList.get(i));
//            }
//            response.put("count", paginatedGameList.size());
//            response.put("size", paginatedGameList.size());
//
//
////            if (games.size() != 0){
//            for(Game game: paginatedGameList){
//                JsonObject gameInfo = new JsonObject();
//                gameInfo.put("id", game.getId());
//                gameInfo.put("name", game.getName());
//                gameInfo.put("type", game.getType());
//                gameInfo.put("description", game.getDescription());
//                gameInfo.put("url", game.getUrl());
//                gameInfo.put("image", game.getImage());
//                gameInfo.put("addAdding", game.getDateAdding());
//                gameInfo.put("useStatistics", game.getUseStatistic());
//                gameInfo.put("gameStatus", game.getGameStatus());
//                gamesList.add(gameInfo);
//            }
////            }
//
//            response.put("results", gamesList);
//            return response);
        }catch (Exception e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return exceptionResponse;
        }
    }

    @GetMapping(path = "/get")
    public JsonObject getGame(@RequestHeader("token") String token,
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
            response.put("useStatistics", game.getUseStatistic());
            response.put("gameStatus", game.getGameActiveStatus());
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("gameInfo", response);
            return jsonObject;
        }catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return exceptionResponse;
        }
    }

    @DeleteMapping(path = "/removing")
    public String removeGame(@RequestHeader("token") String token,
                                   @Valid @RequestBody GameToRemove gameToRemove) {
        try {
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Game game = this.gameService.getGameById(UUID.fromString(gameToRemove.getGame_id()));

            String gameUrl = "/app/games/" + game.getId().toString();
            if (new File(gameUrl).delete())
                System.out.println("game files were deleted");
            else System.out.println("game not found");

            List<GamePatient> gamePatientList = this.gamePatientService.getAllGamePatientsByGame(game);

            //TODO цикл сохранения
            for (GamePatient gamePatient: gamePatientList){
                gamePatient.setGamePatientStatus(GamePatientStatus.DELETED);
                this.gamePatientService.saveGamePatient(gamePatient);
            }

            game.setActiveGameStatus(GameActiveStatus.DELETED);
            game.setUrl(null);
            this.gameService.updateGame(game);
            //Удаление из папки
//            this.gamePatientService.deactivateGame(game);


            return "Game was deleted";
        }
        catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @DeleteMapping(path = "/disable")
    public String deactivateGame(@RequestHeader("token") String token,
                             @Valid @RequestBody GameToRemove gameToRemove) {
        try {
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Game game = this.gameService.getGameById(UUID.fromString(gameToRemove.getGame_id()));

            //TODO перенести в сервис
            List<GamePatient> gamePatientList = this.gamePatientService.getAllGamePatientsByGame(game);
            for (GamePatient gamePatient: gamePatientList){
                gamePatient.setGamePatientStatus(GamePatientStatus.DELETED);
                this.gamePatientService.saveGamePatient(gamePatient);
            }

            game.setActiveGameStatus(GameActiveStatus.DISABLED);
            this.gameService.updateGame(game);
//            this.gamePatientService.deactivateGame(game);
            return "Game was DISABLED";
        }
        catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @PutMapping(path = "/activate")
    public String activateGame(@RequestHeader("token") String token,
                                 @Valid @RequestBody GameToRemove gameToRemove) {
        try {
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Game game = this.gameService.getGameById(UUID.fromString(gameToRemove.getGame_id()));
            game.setActiveGameStatus(GameActiveStatus.ACTIVE);
            this.gameService.updateGame(game);
            return "Game was activated";
        }
        catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @PutMapping(path = "/changing")
    public JsonObject changeGame(@RequestHeader("token") String token,
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
            return response;
        }
        catch (IllegalArgumentException e) {
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return exceptionResponse;
        }
    }
}
