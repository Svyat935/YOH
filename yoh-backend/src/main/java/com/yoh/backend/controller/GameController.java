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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping(path = "/all")
    public ResponseEntity<JsonObject> allGames(@RequestHeader("token") String token,
                                               @RequestParam(value = "regex", required = false, defaultValue = "") String regex,
                                               @RequestParam(value = "typeRegex", required = false, defaultValue = "") String typeRegex,
                                               @RequestParam(value = "limit", required = true) Integer limit,
                                               @RequestParam(value = "start", required = true) Integer start,
                                               @RequestParam(value = "showDeleted", required = false) Boolean showDeleted,
                                               @RequestParam(value = "order", required = false, defaultValue = "1") String order,
                                               @RequestParam(value = "patientID", required = false) String patientID) {
        try{
            User user = this.userService.getUserById(this.userService.verifyToken(token));
//            if (user.getRole() == 1 || user.getRole() == 2)
            if (user.getRole() != 0 && user.getRole() != 3)
                throw new IllegalArgumentException("This user doesn't have permission");
            if (user.getRole() != 0) showDeleted = null;
            JsonObject response = new JsonObject();
            ArrayList<UUID> UUIDList = new ArrayList<>();
            if (patientID != null) this.gamePatientService.getAllGamesByPatient(this.patientService.getPatientById(UUID.fromString(patientID))).forEach(i -> UUIDList.add(i.getId()));
            int listCount = this.gameService.getAllActiveGamesFilteredCount(typeRegex, regex, UUIDList, showDeleted);
            if (listCount == 0) {
                response.put("previous", false);
                response.put("next", false);
                response.put("count", 0);
                response.put("size", 0);
                response.put("results", new ArrayList<>());
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            if (start >= listCount)
                throw new IllegalArgumentException(String.format("No element at that index (%s)", start));
            List<Game> gameList = this.gameService.getAllActiveGamesFiltered(order, typeRegex, regex, UUIDList, start, limit, showDeleted);
            if (start == 0)
                response.put("previous", false);
            else response.put("previous", true);
            if (start + limit > listCount)
                response.put("next", false);
            else response.put("next", true);
            response.put("count", gameList.size());
            response.put("size", listCount);
            response.put("results", gameList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/get")
    public ResponseEntity<JsonObject> getGame(@RequestHeader("token") String token,
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
            return new ResponseEntity<>(jsonObject, HttpStatus.OK);
        }catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "/removing")
    public ResponseEntity<JsonObject> removeGame(@RequestHeader("token") String token,
                                   @Valid @RequestBody GameToRemove gameToRemove) {
        try {
            User userVerification = this.userService.getUserById(this.userService.verifyToken(token));
            if (userVerification.getRole() != 0) {
                JsonObject forbiddenResponse = new JsonObject();
                forbiddenResponse.put("message", "Only admin is allowed");
                return new ResponseEntity<>(forbiddenResponse, HttpStatus.FORBIDDEN);
            }
            Game game = this.gameService.getGameById(UUID.fromString(gameToRemove.getGame_id()));

            String gameUrl = "/app/games/" + game.getId().toString();
            if (new File(gameUrl).delete())
                System.out.println("game files were deleted");
            else System.out.println("game not found");

            List<GamePatient> gamePatientList = this.gamePatientService.getAllGamePatientsByGame(game);

            for (GamePatient gamePatient: gamePatientList){
                gamePatient.setGamePatientStatus(GamePatientStatus.DELETED);
                this.gamePatientService.saveGamePatient(gamePatient);
            }

            game.setActiveGameStatus(GameActiveStatus.DELETED);
            game.setUrl(null);
            this.gameService.updateGame(game);
            JsonObject response = new JsonObject();
            response.put("message", "Game was deleted");
            return new ResponseEntity<>(response, HttpStatus.OK);
            //Удаление из папки
//            this.gamePatientService.deactivateGame(game);

        }
        catch (IllegalArgumentException e) {
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "/disable")
    public ResponseEntity<JsonObject> deactivateGame(@RequestHeader("token") String token,
                             @Valid @RequestBody GameToRemove gameToRemove) {
        try {
            User userVerification = this.userService.getUserById(this.userService.verifyToken(token));
            if (userVerification.getRole() != 0) {
                JsonObject forbiddenResponse = new JsonObject();
                forbiddenResponse.put("message", "Only admin is allowed");
                return new ResponseEntity<>(forbiddenResponse, HttpStatus.FORBIDDEN);
            }
            Game game = this.gameService.getGameById(UUID.fromString(gameToRemove.getGame_id()));

            //TODO перенести в сервис
            List<GamePatient> gamePatientList = this.gamePatientService.getAllGamePatientsByGame(game);
            for (GamePatient gamePatient: gamePatientList){
                gamePatient.setGamePatientStatus(GamePatientStatus.DELETED);
                this.gamePatientService.saveGamePatient(gamePatient);
            }

            game.setActiveGameStatus(GameActiveStatus.DISABLED);
            this.gameService.updateGame(game);
            JsonObject response = new JsonObject();
            response.put("message", "Game was DISABLED");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/activate")
    public ResponseEntity<JsonObject> activateGame(@RequestHeader("token") String token,
                                 @Valid @RequestBody GameToRemove gameToRemove) {
        try {
            User userVerification = this.userService.getUserById(this.userService.verifyToken(token));
            if (userVerification.getRole() != 0) {
                JsonObject forbiddenResponse = new JsonObject();
                forbiddenResponse.put("message", "Only admin is allowed");
                return new ResponseEntity<>(forbiddenResponse, HttpStatus.FORBIDDEN);
            }
            Game game = this.gameService.getGameById(UUID.fromString(gameToRemove.getGame_id()));
            game.setActiveGameStatus(GameActiveStatus.ACTIVE);
            this.gameService.updateGame(game);
//            return "Game was activated";
            JsonObject response = new JsonObject();
            response.put("message", "Game was activated");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/changing")
    public ResponseEntity<JsonObject> changeGame(@RequestHeader("token") String token,
                                   @Valid @RequestBody EditGameRequest editGameRequest) {
        try {
            User userVerification = this.userService.getUserById(this.userService.verifyToken(token));
            if (userVerification.getRole() != 0) {
                JsonObject forbiddenResponse = new JsonObject();
                forbiddenResponse.put("message", "Only admin is allowed");
                return new ResponseEntity<>(forbiddenResponse, HttpStatus.FORBIDDEN);
            }
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
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
