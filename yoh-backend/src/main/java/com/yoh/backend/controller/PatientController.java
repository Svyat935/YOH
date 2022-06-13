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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private GamePatientService gamePatientService;

    @Autowired
    private StartedGameService startedGameService;

    @GetMapping(path = "/games/getting")
    public ResponseEntity<JsonObject> getAllGames(@RequestHeader("token") String token,
                                                  @RequestParam(value = "limit", required = true) Integer limit,
                                                  @RequestParam(value = "start", required = true) Integer start,
                                                  @RequestParam(value = "regex", required = false, defaultValue = "") String regex,
                                                  @RequestParam(value = "typeRegex", required = false, defaultValue = "") String typeRegex,
                                                  @RequestParam(value = "order", required = false, defaultValue = "") String order) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            ArrayList<JsonObject> gamesArray = new ArrayList<>();
            JsonObject response = new JsonObject();
            int listCount = this.gamePatientService.getAllActiveGamesPatientByPatientCount(patient, typeRegex, regex);
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

            List<GamePatient> gamePatientList = this.gamePatientService.getAllActiveGamesPatientByPatientOrderedPaginated(patient, order, typeRegex, regex, start, limit);

            if (start == 0)
                response.put("previous", false);
            else response.put("previous", true);

            if (start + limit > listCount)
                response.put("next", false);
            else response.put("next", true);

            response.put("count", gamePatientList.size());
            response.put("size", listCount);
            for (GamePatient gamePatient: gamePatientList) {
                JsonObject gamesInfo = new JsonObject();
                gamesInfo.put("id", gamePatient.getGame().getId().toString());
                gamesInfo.put("name", gamePatient.getGame().getName());
                gamesInfo.put("type", gamePatient.getGame().getType());
                gamesInfo.put("description", gamePatient.getGame().getDescription());
                gamesInfo.put("image", gamePatient.getGame().getImage());
                gamesInfo.put("url", gamePatient.getGame().getUrl());
                gamesInfo.put("useStatistics", gamePatient.getGame().getUseStatistic());
                gamesInfo.put("assignmentDate", gamePatient.getAssignmentDate());
                gamesInfo.put("assignedBy", gamePatient.getTutor().getId().toString());
                gamesInfo.put("status", gamePatient.getStatus().toString());
                gamesInfo.put("active", gamePatient.getGamePatientStatus().toString());
                gamesArray.add(gamesInfo);
            }
            response.put("results", gamesArray);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping(path = "/games/statistics/test")
    public ResponseEntity<JsonObject> sad(@RequestHeader("token") String token, @RequestHeader("game") String gameID) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Game game = this.gameService.getGameById(UUID.fromString(gameID));
            GamePatient gamePatient = this.gamePatientService.getGamePatientByGameAndPatient(game, patient);
            if (gamePatient == null) throw new IllegalArgumentException("Sorry, but GamePatient was not found.");
            JsonObject response = new JsonObject();
            response.put("result", this.gameStatisticService.sdasdasds(gamePatient));
            response.put("class", this.gameStatisticService.sdasdasds(gamePatient).getClass());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping(path = "/games/statistics/additional_fields")
    public ResponseEntity<JsonObject> getAdditionalFields(@RequestHeader("token") String token,
                                            @RequestHeader("game") String gameID) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Game game = this.gameService.getGameById(UUID.fromString(gameID));
            GamePatient gamePatient = this.gamePatientService.getGamePatientByGameAndPatient(game, patient);
            if (gamePatient == null) throw new IllegalArgumentException("Sorry, but GamePatient was not found.");
            String details = this.startedGameService.getLatestDetailsByGamePatient(gamePatient);
            JsonObject response = new JsonObject();
            response.put("result", details);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/games/statistics/additional_fields")
    public ResponseEntity<JsonObject> setAdditionalFields(@RequestHeader("token") String token,
                                          @RequestHeader("game") String gameID,
                                          @Valid @RequestBody JsonRequest data) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Game game = this.gameService.getGameById(UUID.fromString(gameID));
            GamePatient gamePatient = this.gamePatientService.getGamePatientByGameAndPatient(game, patient);
            if (gamePatient == null) throw new IllegalArgumentException("Sorry, but GamePatient was not found.");
            StartedGame startedGame = this.startedGameService.getLatestStartedGameByGamePatient(gamePatient);
            if (startedGame == null) throw new IllegalArgumentException("Sorry, but StartedGame was not found.");
            startedGame.setDetails(data.getDetails());
            this.startedGameService.saveStartedGame(startedGame);
            JsonObject response = new JsonObject();
            response.put("result", "OK");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(path = "/games/statistics/game_start")
    public ResponseEntity<JsonObject> addGameStart(@RequestHeader("token") String token,
                                     @RequestHeader("game") String gameID,
                                     @Valid @RequestBody GameStartRequest data) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Game game = this.gameService.getGameById(UUID.fromString(gameID));
            GamePatient gamePatient = this.gamePatientService.getGamePatientByGameAndPatient(game, patient);
            if (gamePatient == null) throw new IllegalArgumentException("Sorry, but GamePatient was not found.");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy, h:m:s a");
            LocalDateTime dateStart = LocalDateTime.parse(data.getDate_start(), formatter);


            StartedGame startedGame = new StartedGame(gamePatient, dateStart, null, data.getDetails());
            this.startedGameService.saveStartedGame(startedGame);

            if (gamePatient.getStatus() != Status.DONE) {
                gamePatient.setStatus(Status.STARTED);
                this.gamePatientService.saveGamePatient(gamePatient);
            }

            JsonObject response = new JsonObject();
            response.put("result", "OK");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(path = "/games/statistics/game_end")
    public ResponseEntity<JsonObject> addGameEnd(@RequestHeader("token") String token,
                                   @RequestHeader("game") String gameID,
                                   @Valid @RequestBody GameEndRequest data) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Game game = this.gameService.getGameById(UUID.fromString(gameID));
            GamePatient gamePatient = this.gamePatientService.getGamePatientByGameAndPatient(game, patient);
            if (gamePatient == null) throw new IllegalArgumentException("Sorry, but GamePatient was not found.");
            StartedGame startedGame = this.startedGameService.getUnfinishedStartedGameByGamePatient(gamePatient);
            if (startedGame == null) throw new IllegalArgumentException("Sorry, but StartedGame was not found.");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy, h:m:s a");
            LocalDateTime dateEnd = LocalDateTime.parse(data.getDate_end(), formatter);

            startedGame.setDateEnd(dateEnd);
            startedGame.setDetails(data.getDetails());
            this.startedGameService.saveStartedGame(startedGame);

            if (gamePatient.getStatus() != Status.DONE) {
                gamePatient.setStatus(Status.DONE);
                this.gamePatientService.saveGamePatient(gamePatient);
            }

            JsonObject response = new JsonObject();
            response.put("result", "OK");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(path = "/games/statistics/send_statistic")
    public ResponseEntity<JsonObject> sendStatistic(@RequestHeader("token") String token,
                                      @RequestHeader("game") String gameID,
                                      @Valid @RequestBody StatisticToSend data) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Game game = this.gameService.getGameById(UUID.fromString(gameID));
            GamePatient gamePatient = this.gamePatientService.getGamePatientByGameAndPatient(game, patient);
            StartedGame startedGame = this.startedGameService.getUnfinishedStartedGameByGamePatient(gamePatient);
            if (startedGame == null) throw new IllegalArgumentException("Sorry, but StartedGame was not found.");
            startedGame.setDetails(data.getAdditional_fields());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy, h:m:s a");
            LocalDateTime dateStart = LocalDateTime.parse(data.getDate_start(), formatter);
            LocalDateTime dateEnd = LocalDateTime.parse(data.getDate_end(), formatter);
            GameStatistic gameStatistic = new GameStatistic(
                    startedGame,
                    data.getLevel(),
                    data.getLevel_name(),
                    dateStart,
                    dateEnd,
                    data.getType(),
                    data.getClicks(),
                    data.getMissclicks(),
                    data.getDetails()
            );
            this.startedGameService.saveStartedGame(startedGame);
            this.gameStatisticService.saveGameStatistic(gameStatistic);

            JsonObject response = new JsonObject();
            response.put("result", "OK");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping(path = "/games/status")
    public ResponseEntity<JsonObject> getStatusOfGame(@RequestHeader("token") String token,
                                        @RequestParam String gameID) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
//            GameStatus gameStatus = this.gameStatusService.getGameStatusByGameAndPatient(this.gameService.getGameById(UUID.fromString(gameID)), patient);
            Game game = this.gameService.getGameById(UUID.fromString(gameID));
            GamePatient gamePatient = this.gamePatientService.getGamePatientByGameAndPatient(game, patient);
//            if (gamePatient == null) throw new IllegalArgumentException("Sorry, but GamePatient was not found.");
//            GameStatus gameStatus = this.gameStatusService.getGameStatusByGamePatient(gamePatient);
            JsonObject response = new JsonObject();
            response.put("gameStatus", gamePatient.getStatus());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/games/status/update")
    public ResponseEntity<JsonObject> updateStatusOfGame(@RequestHeader("token") String token,
                                           @Valid @RequestBody StatusRequest statusRequest) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            JsonObject response = new JsonObject();
            response.put("message", "Statuses are set automatically");
            return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/games/status/statistic")
    public ResponseEntity<JsonObject> getStatusesStatistic(@RequestHeader("token") String token) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            JsonObject response = new JsonObject();
            int done = 0;
            int assigned = 0;
            int failed = 0;
            int started = 0;
            for (GamePatient gamePatient: this.gamePatientService.getAllActiveGamePatientsByPatient(patient)){
                switch (gamePatient.getStatus()){
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
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/tests/getting")
    public ResponseEntity<JsonObject> getAllTests(@RequestHeader("token") String token) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            JsonObject response = new JsonObject();
            response.put("message", "Stub");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/tests/statistics/sending")
    public ResponseEntity<JsonObject> sendStatisticOfGame(@RequestHeader("token") String token,
                                            @Valid @RequestBody TestStatisticToSend testStatisticToSend) {
        try {
            JsonObject response = new JsonObject();
            response.put("message", "In work");
            return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping(path = "/tests/status/update")
    public ResponseEntity<JsonObject> updateStatusOfTest(@RequestHeader("token") String token,
                                           @Valid @RequestBody StatusRequest statusRequest) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            JsonObject response = new JsonObject();
            response.put("message", "Status of the test was updated");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/account")
    public ResponseEntity<JsonObject> getAccountInfo(@RequestHeader("token") String token) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            if (patient == null) throw new IllegalArgumentException("Patient not found");
            JsonObject response = new JsonObject();

            response.put("id", patient.getId().toString());
            response.put("name", patient.getName());
            response.put("surname", patient.getSurname());
            response.put("secondName", patient.getSecondName());
            if (patient.getGender() != null)
                response.put("gender", patient.getGender().toString());
            else response.put("gender", null);
            if (patient.getOrganization() != null) {
                response.put("organization", patient.getOrganization().getId().toString());
                response.put("organizationString", patient.getOrganization().getName());
            }
            else {
                response.put("organization", null);
                response.put("organizationString", null);
            }

            if (patient.getBirthDate() != null)
                response.put("birthDate", patient.getBirthDate().toString());
            else response.put("birthDate", null);
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
                if (tutor.getOrganization() != null) {
                    tutorInfo.put("organization", tutor.getOrganization().getId().toString());
                    tutorInfo.put("organizationString", tutor.getOrganization().getName());
                }
                else {
                    tutorInfo.put("organization", null);
                    tutorInfo.put("organizationString", null);
                }
                tutorInfo.put("login", tutor.getUser().getLogin());
                tutorInfo.put("email", tutor.getUser().getEmail());
                response.put("tutor", tutorInfo);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/account/image/add")
    public ResponseEntity<JsonObject> uploadPatientImage(@RequestHeader("token") String token,
                                           @RequestParam("image") MultipartFile file) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            String orgName = UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            if(patient.getImage() != null){
                new File("/app/images/" + patient.getImage()).delete();
                System.out.println("Old image was deleted");
            }
            File filesd = new File("/app/images", orgName);
            FileUtils.writeByteArrayToFile(filesd, file.getBytes());

//            String url = orgName;
            patient.setImage(orgName);
            this.patientService.updatePatient(patient);
            JsonObject response = new JsonObject();
            response.put("message", "Patient account image was added");
            response.put("image", orgName);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping(path = "/account/image/delete")
    public ResponseEntity<JsonObject> deletePatientImage(@RequestHeader("token") String token) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            if (patient == null) throw new IllegalArgumentException("Patient was not found");
            if(patient.getImage() != null){
                new File("/app/images/" + patient.getImage()).delete();
                System.out.println("Old image was deleted");
                patient.setImage(null);
                this.patientService.updatePatient(patient);
                JsonObject response = new JsonObject();
                response.put("message", "Patient account image was deleted");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            else {
                JsonObject response = new JsonObject();
                response.put("message", "Patient image is already deleted");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
        catch (Exception e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/account/image")
    public ResponseEntity<JsonObject> getPatientImage(@RequestHeader("token") String token) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            if (patient == null) throw new IllegalArgumentException("Patient not found");
            if (patient.getImage() != null) {
                JsonObject response = new JsonObject();
                response.put("image", patient.getImage());
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            else {
                JsonObject response = new JsonObject();
                response.put("message", "Patient does not have an image");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/account/changing")
    public ResponseEntity<JsonObject> editAccountOfPatient(@RequestHeader("token") String token,
                                             @Valid @RequestBody EditPatientInfoRequest editPatientInfoRequest) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            if (patient == null) throw new IllegalArgumentException("Patient not found");
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
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
