package com.yoh.backend.controller;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.yoh.backend.entity.*;
import com.yoh.backend.enums.Gender;
import com.yoh.backend.enums.Status;
import com.yoh.backend.request.*;
import com.yoh.backend.response.*;
import com.yoh.backend.service.*;
import com.yoh.backend.util.ImageUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patient")
public class PatientController {

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

    @GetMapping(path = "/games/getting")
    public JSONResponse getAllGames(@RequestHeader("token") String token) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            ArrayList<JsonObject> gamesArray = new ArrayList<>();
            if (patient.getGames() != null){
                for (Game game: patient.getGames()){
                    JsonObject gamesInfo = new JsonObject();
                    gamesInfo.put("id", game.getId().toString());
                    gamesInfo.put("name", game.getName());
                    gamesInfo.put("description", game.getDescription());
                    gamesInfo.put("url", game.getUrl());
                    gamesArray.add(gamesInfo);
                }
            }
            JsonObject response = new JsonObject();
            response.put("gamesArray", gamesArray);
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @PostMapping(path = "/games/statistics/sending")
    public JSONResponse sendGameStatistic(@RequestHeader("token") String token,
                                          @RequestHeader("game") String game,
                                          @Valid @RequestBody StatisticArray statisticArray) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            System.out.println(statisticArray.getRecords());
            for (JsonObject statisticToSend: statisticArray.getRecords()){
                System.out.println(statisticToSend.get("DateAction"));
                System.out.println(statisticToSend.get("Type"));
                System.out.println(statisticToSend.get("AnswerNumber"));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy, h:m:s a");
                LocalDateTime localDateTime = LocalDateTime.parse(statisticToSend.get("DateAction").toString(), formatter);

                Short AnswerNumber;
                if (statisticToSend.get("AnswerNumber") != null)
                    AnswerNumber = Short.valueOf(statisticToSend.get("AnswerNumber").toString());
                else
                    AnswerNumber = null;

                GameStatistic statistic = new GameStatistic(
                        this.gameService.getGameById(UUID.fromString(game)),
                        patient,
                        Short.valueOf(statisticToSend.get("Type").toString()),
                        localDateTime,
                        AnswerNumber
                );
                this.gameStatisticService.createGameStatistic(statistic);
                patient.getGameStatistics().add(statistic);
                this.patientService.updatePatient(patient);
            }
//            GameStatistic statistic = new GameStatistic(
//                    this.gameService.getGameById(UUID.fromString(gameStatisticToSend.getGame_id())),
//                    patient,
//                    gameStatisticToSend.getType(),
//                    gameStatisticToSend.getDateAction(),
//                    gameStatisticToSend.getMessage()
//            );
//            this.gameStatisticService.createGameStatistic(statistic);
//            patient.getGameStatistics().add(statistic);
//            this.patientService.updatePatient(patient);
            JsonObject response = new JsonObject();
            response.put("message", "GameStatistic was added");
            return new JSONResponse(200, response);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
//        catch (IllegalArgumentException e){
//            JsonObject exceptionResponse = new JsonObject();
//            exceptionResponse.put("message", e.getMessage());
//            return new JSONResponse(401, exceptionResponse);
//        }
    }

    @GetMapping(path = "/games/status")
    public JSONResponse getStatusOfGame(@RequestHeader("token") String token,
                                        @RequestParam String gameID) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            GameStatus gameStatus = this.gameStatusService.getGameStatusByGameAndPatient(this.gameService.getGameById(UUID.fromString(gameID)), patient);
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
            List<GameStatus> gameStatusList = patient.getGameStatuses();
            for (GameStatus gameStatus: gameStatusList) {
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
            byte[] imageBytes = ImageUtility.compressImage(file.getBytes());
            patient.setImage(imageBytes);
            this.patientService.updatePatient(patient);
            JsonObject response = new JsonObject();
            response.put("message", "Patient account image was added");
            return new JSONResponse(200, response);
        }
        catch (Exception e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @PutMapping(path = "/account/image/edit")
    public JSONResponse updatePatientImage(@RequestHeader("token") String token,
                                           @RequestParam("image") MultipartFile file) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            byte[] imageBytes = ImageUtility.compressImage(file.getBytes());
            patient.setImage(imageBytes);
            this.patientService.updatePatient(patient);
            JsonObject response = new JsonObject();
            response.put("message", "Patient account image was edited");
            return new JSONResponse(200, response);
        }
        catch (Exception e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

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
                response.put("image", ImageUtility.decompressImage(patient.getImage()));
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
