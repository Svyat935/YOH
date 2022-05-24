package com.yoh.backend.controller;

import com.github.cliftonlabs.json_simple.JsonKey;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.google.gson.JsonParser;
import com.yoh.backend.entity.*;
import com.yoh.backend.enums.GamePatientStatus;
import com.yoh.backend.enums.Gender;
import com.yoh.backend.enums.Status;
import com.yoh.backend.request.*;
import com.yoh.backend.response.*;
import com.yoh.backend.service.*;
import com.yoh.backend.util.ImageUtility;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.json.bind.Jsonb;
import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/patient")
public class PatientController {

//    @Value("${IMAGE_FOLDER}")
//    private String image_folder;

    @Value("${SITE_URL}")
    private String site_url;

    @Autowired
    private PatientService patientService;

    @Autowired
    private GameService gameService;

    @Autowired
    private TestService testService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private GameStatisticService gameStatisticService;

    @Autowired
    private TestStatisticService testStatisticService;

    @Autowired
    private GameStatusService gameStatusService;

    @Autowired
    private TestStatusService testStatusService;

    @Autowired
    private GamePatientService gamePatientService;

    @Autowired
    private StartedGameService startedGameService;

    @GetMapping(path = "/games/getting")
    public JSONResponse getAllGames(@RequestHeader("token") String token,
                                    @RequestParam(value = "limit", required = true) Integer limit,
                                    @RequestParam(value = "start", required = true) Integer start,
                                    @RequestParam(value = "regex", required = false, defaultValue = "") String regex,
                                    @RequestParam(value = "typeRegex", required = false, defaultValue = "") String typeRegex,
                                    @RequestParam(value = "order", required = false, defaultValue = "") String order) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            ArrayList<JsonObject> gamesArray = new ArrayList<>();
            JsonObject response = new JsonObject();
//            List<Game> gameList = patient.getGames().stream().filter(i -> i.getName().toLowerCase().contains(regex.toLowerCase())
//                            && i.getType().toLowerCase().contains(typeRegex.toLowerCase())).collect(Collectors.toList());
            List<GamePatient> gamePatientList = this.gamePatientService.getActiveGamePatientByPatient(patient, order);
            if (!typeRegex.equals("")){
                gamePatientList = gamePatientList.stream().filter(i -> i.getGame().getType().toLowerCase().contains(typeRegex.toLowerCase())).collect(Collectors.toList());
            }
            if (!regex.equals(""))
                gamePatientList = gamePatientList.stream().filter(i -> i.getGame().getName().toLowerCase().contains(regex.toLowerCase())).collect(Collectors.toList());
            if (gamePatientList.size() == 0) {
//                JsonObject response = new JsonObject();
                response.put("previous", false);
                response.put("next", false);
                response.put("count", 0);
                response.put("results", new ArrayList<>());
                return new JSONResponse(200, response);
            }


            //Pagination
            if (start >= gamePatientList.size())
                throw new IllegalArgumentException(
                        String.format("No element at that index (%s)", start)
                );
            int lastIndex;
            if (start + limit > gamePatientList.size()){
                lastIndex = gamePatientList.size();
                response.put("next", false);
            }
            else {
                lastIndex = start + limit;
                response.put("next", true);
            }
            if (start == 0) response.put("previous", false);
            else response.put("previous", true);
            List<GamePatient> paginatedGamesList = new ArrayList<>();
            for (int i = start; i < lastIndex; i++){
                paginatedGamesList.add(gamePatientList.get(i));
            }
            response.put("count", paginatedGamesList.size());

            for (GamePatient gamePatient: paginatedGamesList) {
                JsonObject gamesInfo = new JsonObject();
                GameStatus gameStatus = this.gameStatusService.getGameStatusByGamePatient(gamePatient);
                gamesInfo.put("id", gamePatient.getGame().getId().toString());
                gamesInfo.put("name", gamePatient.getGame().getName());
                gamesInfo.put("type", gamePatient.getGame().getType());
                gamesInfo.put("description", gamePatient.getGame().getDescription());
                gamesInfo.put("image", gamePatient.getGame().getImage());
                gamesInfo.put("url", gamePatient.getGame().getUrl());
                gamesInfo.put("assignmentDate", gameStatus.getAssignmentDate());
                gamesInfo.put("assignedBy", gameStatus.getTutor().getId().toString());
                gamesInfo.put("status", gameStatus.getStatus().toString());
                gamesInfo.put("active", gamePatient.getGamePatientStatus().toString());
                gamesArray.add(gamesInfo);
            }
            response.put("results", gamesArray);
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

//    @PostMapping(path = "/games/statistics/sending")
//    public JSONResponse sendGameStatistic(@RequestHeader("token") String token,
//                                          @RequestHeader("game") String gameID,
//                                          @Valid @RequestBody StatisticArray statisticArray) {
//        try {
//            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
//            Game game = this.gameService.getGameById(UUID.fromString(gameID));
//            GamePatient gamePatient = this.gamePatientService.getGamePatientByGameAndPatient(game, patient);
//            if (!gamePatient.getGamePatientStatus().equals(GamePatientStatus.ACTIVE))
//                throw new Exception("Game is not Active for this account");
//            for (JsonObject statisticToSend: statisticArray.getRecords()){
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy, h:m:s a");
//                LocalDateTime dateAction = LocalDateTime.parse(statisticToSend.get("DateAction").toString(), formatter);
//                com.google.gson.JsonObject jsonObject;
//
//                String detailsString;
//                if (statisticToSend.containsKey("Details")){
//                    jsonObject = new JsonParser().parse(statisticToSend.get("Details").toString()).getAsJsonObject();
//                }
//                else jsonObject = null;
//
//                Short AnswerNumber;
//                if (statisticToSend.get("AnswerNumber") != null)
//                    AnswerNumber = Short.valueOf(statisticToSend.get("AnswerNumber").toString());
//                else
//                    AnswerNumber = null;
//
//                LocalDateTime dateStart;
//                if (statisticToSend.get("DateStart") != null)
//                    dateStart = LocalDateTime.parse(statisticToSend.get("DateStart").toString(), formatter);
//                else
//                    dateStart = null;
//
//                Integer clicks;
//                if (statisticToSend.get("Clicks") != null)
//                    clicks = Integer.valueOf(statisticToSend.get("Clicks").toString());
//                else clicks = null;
//
//                Integer missClicks;
//                if (statisticToSend.get("MissClicks") != null)
//                    missClicks = Integer.valueOf(statisticToSend.get("MissClicks").toString());
//                else missClicks = null;
//
//
//                if (jsonObject != null) {
//                    detailsString = jsonObject.toString();
//                }
//                else detailsString = null;
//
//                Short type = Short.valueOf(statisticToSend.get("Type").toString());
//                //Ставим статус завершения при типе 2
//                if (type == 2){
//                    GameStatus gameStatus = this.gameStatusService.getGameStatusByGamePatient(gamePatient);
//                    gameStatus.setStatus(Status.DONE);
//                    this.gameStatusService.updateGameStatus(gameStatus);
//                }
//
//                GameStatistic statistic = new GameStatistic(
//                        gamePatient,
//                        type,
//                        dateAction,
//                        AnswerNumber,
//                        dateStart,
//                        clicks,
//                        missClicks,
//                        detailsString);
//                this.gameStatisticService.createGameStatistic(statistic);
//                this.patientService.updatePatient(patient);
//            }
//            JsonObject response = new JsonObject();
//            response.put("message", "GameStatistic was added");
//            return new JSONResponse(200, response);
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//            JsonObject exceptionResponse = new JsonObject();
//            exceptionResponse.put("message", e.getMessage());
//            return new JSONResponse(401, exceptionResponse);
//        }
//    }


    @GetMapping(path = "/games/statistics/test")
    public JSONResponse sad(@RequestHeader("token") String token, @RequestHeader("game") String gameID) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Game game = this.gameService.getGameById(UUID.fromString(gameID));
            GamePatient gamePatient = this.gamePatientService.getGamePatientByGameAndPatient(game, patient);
            if (gamePatient == null) throw new IllegalArgumentException("Sorry, but GamePatient was not found.");
            JsonObject response = new JsonObject();
            response.put("result", this.gameStatisticService.sdasdasds(gamePatient));
            response.put("class", this.gameStatisticService.sdasdasds(gamePatient).getClass());
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }


    @GetMapping(path = "/games/statistics/additional_fields")
    public JSONResponse getAdditionalFields(@RequestHeader("token") String token,
                                            @RequestHeader("game") String gameID) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Game game = this.gameService.getGameById(UUID.fromString(gameID));
            GamePatient gamePatient = this.gamePatientService.getGamePatientByGameAndPatient(game, patient);
            if (gamePatient == null) throw new IllegalArgumentException("Sorry, but GamePatient was not found.");
            String details = this.startedGameService.getLatestDetailsByGamePatient(gamePatient);
            JsonObject response = new JsonObject();
            response.put("result", details);
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @PostMapping(path = "/games/statistics/additional_fields")
    public JSONResponse setAdditionalFields(@RequestHeader("token") String token,
                                            @RequestHeader("game") String gameID,
                                            @Valid @RequestBody JsonRequest data) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Game game = this.gameService.getGameById(UUID.fromString(gameID));
            GamePatient gamePatient = this.gamePatientService.getGamePatientByGameAndPatient(game, patient);
            if (gamePatient == null) throw new IllegalArgumentException("Sorry, but GamePatient was not found.");
            StartedGame startedGame = this.startedGameService.getLatestStartedGameByGamePatient(gamePatient);
            if (startedGame == null) throw new IllegalArgumentException("Sorry, but StartedGame was not found.");
            startedGame.setDetails(data.getJsonObject().toString());
            this.startedGameService.saveStartedGame(startedGame);
            JsonObject response = new JsonObject();
            response.put("result", "OK");
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }


//    @PostMapping(path = "/games/statistics/game_start")
//    public JSONResponse addGameStart(@RequestHeader("token") String token,
//                                     @RequestHeader("game") String gameID,
//                                     @Valid @RequestBody GameStartRequest data) {
//        try {
//            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
//            Game game = this.gameService.getGameById(UUID.fromString(gameID));
//            GamePatient gamePatient = this.gamePatientService.getGamePatientByGameAndPatient(game, patient);
//            if (gamePatient == null) throw new IllegalArgumentException("Sorry, but GamePatient was not found.");
//
//            LocalDateTime dateStart = LocalDateTime.parse(data.getDate_start());
//
////            String detailsString;
////            if (data.getDetails() != null) {
////                detailsString = data.getDetails().toString();
////            }
////            else detailsString = null;
//
//            StartedGame startedGame = new StartedGame(gamePatient, dateStart, null, data.getDetails());
//            this.startedGameService.saveStartedGame(startedGame);
//
//            JsonObject response = new JsonObject();
//            response.put("result", "OK");
//            return new JSONResponse(200, response);
//        }
//        catch (IllegalArgumentException e){
//            JsonObject exceptionResponse = new JsonObject();
//            exceptionResponse.put("message", e.getMessage());
//            return new JSONResponse(401, exceptionResponse);
//        }
//    }

    @PostMapping(path = "/games/statistics/game_start")
    public JSONResponse addGameStart(@RequestHeader("token") String token,
                                     @RequestHeader("game") String gameID,
                                     @Valid @RequestBody JsonObject data) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Game game = this.gameService.getGameById(UUID.fromString(gameID));
            GamePatient gamePatient = this.gamePatientService.getGamePatientByGameAndPatient(game, patient);
            if (gamePatient == null) throw new IllegalArgumentException("Sorry, but GamePatient was not found.");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy, h:m:s a");
            LocalDateTime dateStart = LocalDateTime.parse(data.get("date_start").toString(), formatter);
//            LocalDateTime dateStart = LocalDateTime.parse(data.get("date_start").toString());

//            String detailsString;
//            if (data.getDetails() != null) {
//                detailsString = data.getDetails().toString();
//            }
//            else detailsString = null;

            StartedGame startedGame = new StartedGame(gamePatient, dateStart, null, data.get("details").toString());
            this.startedGameService.saveStartedGame(startedGame);

            JsonObject response = new JsonObject();
            response.put("result", "OK");
            return new JSONResponse(200, response);
        }
        catch (Exception e){
            e.printStackTrace();
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @PostMapping(path = "/games/statistics/game_end")
    public JSONResponse addGameEnd(@RequestHeader("token") String token,
                                   @RequestHeader("game") String gameID,
                                   @Valid @RequestBody JsonObject data) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Game game = this.gameService.getGameById(UUID.fromString(gameID));
            GamePatient gamePatient = this.gamePatientService.getGamePatientByGameAndPatient(game, patient);
            if (gamePatient == null) throw new IllegalArgumentException("Sorry, but GamePatient was not found.");
            StartedGame startedGame = this.startedGameService.getUnfinishedStartedGameByGamePatient(gamePatient);
            if (startedGame == null) throw new IllegalArgumentException("Sorry, but StartedGame was not found.");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy, h:m:s a");
            LocalDateTime dateEnd = LocalDateTime.parse(data.get("date_end").toString(), formatter);

//            LocalDateTime dateEnd = LocalDateTime.parse(data.getDate_end());

            startedGame.setDateEnd(dateEnd);
            startedGame.setDetails(data.get("details").toString());
            this.startedGameService.saveStartedGame(startedGame);

            JsonObject response = new JsonObject();
            response.put("result", "OK");
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }


    @PostMapping(path = "/games/statistics/send_statistic")
    public JSONResponse sendStatistic(@RequestHeader("token") String token,
                                      @RequestHeader("game") String gameID,
                                      @Valid @RequestBody JsonObject data) {
        try {
            System.out.println("1231232131231231232");
            System.out.println(data);
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Game game = this.gameService.getGameById(UUID.fromString(gameID));
            GamePatient gamePatient = this.gamePatientService.getGamePatientByGameAndPatient(game, patient);
            StartedGame startedGame = this.startedGameService.getUnfinishedStartedGameByGamePatient(gamePatient);
            if (startedGame == null) throw new IllegalArgumentException("Sorry, but StartedGame was not found.");
            startedGame.setDetails(data.get("details").toString());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy, h:m:s a");
            LocalDateTime dateStart = LocalDateTime.parse(data.get("date_start").toString(), formatter);
            LocalDateTime dateEnd = LocalDateTime.parse(data.get("date_end").toString(), formatter);

//            GameStatistic gameStatistic = new GameStatistic(
//                    startedGame,
//                    statisticToSend.getLevel(),
//                    statisticToSend.getLevel_name(),
//                    dateStart,
//                    dateEnd,
//                    statisticToSend.getType(),
//                    statisticToSend.getClicks(),
//                    statisticToSend.getMissclicks(),
//                    statisticToSend.getDetails()
//            );
            System.out.println(data.get("level_name").toString());
            System.out.println("");

            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();

            System.out.println(data.get("level"));
            System.out.println(data.get("level_name"));
            System.out.println(dateStart);
            System.out.println(dateEnd);
            System.out.println(data.get("type"));
            System.out.println(data.get("clicks"));
            System.out.println(data.get("missclicks"));
            System.out.println(data.get("details"));


            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();

            GameStatistic gameStatistic = new GameStatistic(
                    startedGame,
                    (Integer) data.get("level"),
                    data.get("level_name").toString(),
                    dateStart,
                    dateEnd,
                    (Integer) data.get("type"),
                    (Integer) data.get("clicks"),
                    (Integer) data.get("missclicks"),
                    data.get("details").toString()
            );
            this.startedGameService.saveStartedGame(startedGame);
            this.gameStatisticService.saveGameStatistic(gameStatistic);

            JsonObject response = new JsonObject();
            response.put("result", "OK");
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }


    @GetMapping(path = "/games/status")
    public JSONResponse getStatusOfGame(@RequestHeader("token") String token,
                                        @RequestParam String gameID) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
//            GameStatus gameStatus = this.gameStatusService.getGameStatusByGameAndPatient(this.gameService.getGameById(UUID.fromString(gameID)), patient);
            Game game = this.gameService.getGameById(UUID.fromString(gameID));
            GamePatient gamePatient = this.gamePatientService.getGamePatientByGameAndPatient(game, patient);
            if (gamePatient == null) throw new IllegalArgumentException("Sorry, but GamePatient was not found.");
            GameStatus gameStatus = this.gameStatusService.getGameStatusByGamePatient(gamePatient);
            JsonObject response = new JsonObject();
            response.put("gameStatus", gameStatus.getStatus());
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @PutMapping(path = "/games/status/update")
    public JSONResponse updateStatusOfGame(@RequestHeader("token") String token,
                                           @Valid @RequestBody StatusRequest statusRequest) {
        // TODO Сделать это когда-нибудь
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            JsonObject response = new JsonObject();
            response.put("message", "Status of the game was updated");
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @GetMapping(path = "/games/status/statistic")
    public JSONResponse getStatusesStatistic(@RequestHeader("token") String token) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            JsonObject response = new JsonObject();
            int done = 0;
            int assigned = 0;
            int failed = 0;
            int started = 0;
//            List<GameStatus> gameStatusList = patient.getGameStatuses();
//            for (GameStatus gameStatus: gameStatusList) {
//                switch (gameStatus.getStatus()){
//                    case DONE -> done++;
//                    case ASSIGNED -> assigned++;
//                    case FAILED -> failed++;
//                    case STARTED -> started++;
//                }
//            }
            for (GamePatient gamePatient: this.gamePatientService.getAllActiveGamePatientsByPatient(patient)){
                GameStatus gameStatus = this.gameStatusService.getGameStatusByGamePatient(gamePatient);
                switch (gameStatus.getStatus()){
                    case DONE -> done++;
                    case ASSIGNED -> assigned++;
                    case FAILED -> failed++;
                    case STARTED -> started++;
                }

            }
            response.put("Done", done);
            response.put("Assigned", assigned);
            response.put("Failed", failed);
            response.put("Started", started);
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @GetMapping(path = "/tests/getting")
    public JSONResponse getAllTests(@RequestHeader("token") String token) {
        // TODO Уточнить как тесты возвращаться будут
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            JsonObject response = new JsonObject();
            response.put("message", "Stub");
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @PostMapping(path = "/tests/statistics/sending")
    public JSONResponse sendStatisticOfGame(@RequestHeader("token") String token,
                                            @Valid @RequestBody TestStatisticToSend testStatisticToSend) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            TestStatistic statistic = new TestStatistic(
                    this.testService.getTestById(UUID.fromString(testStatisticToSend.getTest_id())),
                    patient,
                    testStatisticToSend.getType(),
                    testStatisticToSend.getDateAction(),
                    testStatisticToSend.getMessage()
            );
            this.testStatisticService.createTestStatistic(statistic);
            patient.getTestStatistics().add(statistic);
            this.patientService.updatePatient(patient);
            JsonObject response = new JsonObject();
            response.put("message", "TestStatistic was added");
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @GetMapping(path = "/tests/status")
    public JSONResponse getStatusOfTest(@RequestHeader("token") String token,
                                        @RequestParam String testID) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            TestStatus testStatus = this.testStatusService.getTestStatusByTestAndPatient(this.testService.getTestById(UUID.fromString(testID)), patient);
            JsonObject response = new JsonObject();
            response.put("testStatus", testStatus.getStatus());
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @PutMapping(path = "/tests/status/update")
    public JSONResponse updateStatusOfTest(@RequestHeader("token") String token,
                                           @Valid @RequestBody StatusRequest statusRequest) {
        try {
            // TODO Сделать это когда-нибудь
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            JsonObject response = new JsonObject();
            response.put("message", "Status of the test was updated");
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @GetMapping(path = "/account")
    public JSONResponse getAccountInfo(@RequestHeader("token") String token) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            JsonObject response = new JsonObject();

            response.put("id", patient.getId().toString());
            response.put("name", patient.getName());
            response.put("surname", patient.getSurname());
            response.put("secondName", patient.getSecondName());
            if (patient.getGender() != null)
                response.put("gender", patient.getGender().toString());
            else response.put("gender", null);
            if (patient.getOrganization() != null)
                response.put("organization", patient.getOrganization().getId().toString());
            else response.put("organization", null);
            response.put("organizationString", patient.getOrganizationString());
            if (patient.getBirthDate() != null)
                response.put("birthDate", patient.getBirthDate().toString());
            else response.put("birthDate", null);
//            response.put("birthDate", patient.getBirthDate());
            response.put("numberPhone", patient.getNumberPhone());
            response.put("address", patient.getAddress());
            response.put("login", patient.getUser().getLogin());
            response.put("email", patient.getUser().getEmail());
            response.put("image", patient.getImage());

            JsonObject tutorInfo = new JsonObject();
            Tutor tutor = patient.getTutor();
            if (tutor != null){
                tutorInfo.put("id", tutor.getId().toString());
                tutorInfo.put("name", tutor.getName());
                tutorInfo.put("surname", tutor.getSurname());
                tutorInfo.put("secondName", tutor.getSecondName());
                if (tutor.getOrganization() != null)
                    tutorInfo.put("organization", tutor.getOrganization().getId().toString());
                else tutorInfo.put("organization", null);
                tutorInfo.put("organizationString", tutor.getOrganizationString());
                tutorInfo.put("login", tutor.getUser().getLogin());
                tutorInfo.put("email", tutor.getUser().getEmail());
                response.put("tutor", tutorInfo);
            }
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @PostMapping(path = "/account/image/add")
    public JSONResponse uploadPatientImage(@RequestHeader("token") String token,
                                           @RequestParam("image") MultipartFile file) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));

            String orgName = UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
//            String orgName = patient.getId().toString() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
//            Path filepath = Paths.get("/app/images", orgName);
//            if(new  File(filepath.toString()).exists()){
//                System.out.println("File exists");
//                new File(filepath.toString()).delete();
//            }
            if(patient.getImage() != null){
                new File("/app/images/" + patient.getImage()).delete();
                System.out.println("Old image was deleted");
            }
            File filesd = new File("/app/images", orgName);
            FileUtils.writeByteArrayToFile(filesd, file.getBytes());

//            String url = orgName;
            patient.setImage(orgName);
            this.patientService.updatePatient(patient);
            System.out.println(patient.getImage());
            JsonObject response = new JsonObject();
            response.put("message", "Patient account image was added");
            response.put("image", orgName);
            System.out.println();
            System.out.println(patient.getImage());
            System.out.println();
            return new JSONResponse(200, response);
        }
        catch (Exception e){
            e.printStackTrace();
//            System.out.println(e.getMessage());
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

//    @PutMapping(path = "/account/image/edit")
//    public JSONResponse updatePatientImage(@RequestHeader("token") String token,
//                                           @RequestParam("image") MultipartFile file) {
//        try {
//            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
//            byte[] imageBytes = ImageUtility.compressImage(file.getBytes());
//            patient.setImage(imageBytes);
//            this.patientService.updatePatient(patient);
//            JsonObject response = new JsonObject();
//            response.put("message", "Patient account image was edited");
//            return new JSONResponse(200, response);
//        }
//        catch (Exception e){
//            JsonObject exceptionResponse = new JsonObject();
//            exceptionResponse.put("message", e.getMessage());
//            return new JSONResponse(401, exceptionResponse);
//        }
//    }

    @DeleteMapping(path = "/account/image/delete")
    public JSONResponse deletePatientImage(@RequestHeader("token") String token) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
//            byte[] imageBytes = null;
//            patient.setImage(imageBytes);
            patient.setImage(null);
            this.patientService.updatePatient(patient);
            JsonObject response = new JsonObject();
            response.put("message", "Patient account image was deleted");
            return new JSONResponse(200, response);
        }
        catch (Exception e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @GetMapping(path = "/account/image")
    public JSONResponse getPatientImage(@RequestHeader("token") String token) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            if (patient.getImage() != null) {
                JsonObject response = new JsonObject();
                response.put("image", patient.getImage());
                return new JSONResponse(200, response);
            }
            else {
                JsonObject response = new JsonObject();
                response.put("message", "Patient does not have an image");
                return new JSONResponse(200, response);
            }
        }
        catch (Exception e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @PutMapping(path = "/account/changing")
    public JSONResponse editAccountOfPatient(@RequestHeader("token") String token,
                                             @Valid @RequestBody EditPatientInfoRequest editPatientInfoRequest) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            if (editPatientInfoRequest.getName() != null){
                patient.setName(editPatientInfoRequest.getName());
            }
            if (editPatientInfoRequest.getSurname() != null){
                patient.setSurname(editPatientInfoRequest.getSurname());
            }
            if (editPatientInfoRequest.getSecondName() != null){
                patient.setSecondName(editPatientInfoRequest.getSecondName());
            }
            if (editPatientInfoRequest.getGender() != null){
                patient.setGender(Gender.valueOf(editPatientInfoRequest.getGender()));
            }
//            if (editPatientInfoRequest.getOrganization() != null){
//                patient.setOrganization(this.organizationService.getOrganizationById(UUID.fromString(editPatientInfoRequest.getOrganization())));
//            }
            if (editPatientInfoRequest.getBirthDate() != null){
                patient.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse(editPatientInfoRequest.getBirthDate()));
            }
            if (editPatientInfoRequest.getNumberPhone() != null){
                patient.setNumberPhone(editPatientInfoRequest.getNumberPhone());
            }
            if (editPatientInfoRequest.getAddress() != null){
                patient.setAddress(editPatientInfoRequest.getAddress());
            }
            this.patientService.updatePatient(patient);
            JsonObject response = new JsonObject();
            response.put("message", "Patient account was edited");
            return new JSONResponse(200, response);
        }
        catch (Exception e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }
}
