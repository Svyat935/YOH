package com.yoh.backend.controller;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.yoh.backend.entity.*;
import com.yoh.backend.enums.Gender;
import com.yoh.backend.request.EditPatientInfoRequest;
import com.yoh.backend.request.GameStatisticToSend;
import com.yoh.backend.request.StatusRequest;
import com.yoh.backend.request.TestStatisticToSend;
import com.yoh.backend.response.*;
import com.yoh.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
        // TODO Уточнить как игры возвращаться будут.
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

    @PostMapping(path = "/games/statistics/sending")
    public JSONResponse sendGameStatistic(@RequestHeader("token") String token, @Valid @RequestBody GameStatisticToSend gameStatisticToSend) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            GameStatistic statistic = new GameStatistic(
                    this.gameService.getGameById(UUID.fromString(gameStatisticToSend.getGame_id())),
                    patient,
                    gameStatisticToSend.getType(),
                    gameStatisticToSend.getDateAction(),
                    gameStatisticToSend.getMessage()
            );
            this.gameStatisticService.createGameStatistic(statistic);
            patient.getGameStatistics().add(statistic);
            this.patientService.updatePatient(patient);
            JsonObject response = new JsonObject();
            response.put("message", "GameStatistic was added");
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @GetMapping(path = "/games/status")
    public JSONResponse getStatusOfGame(@RequestHeader("token") String token, @RequestParam UUID game_id) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            GameStatus gameStatus = this.gameStatusService.getGameStatusByGameAndPatient(this.gameService.getGameById(game_id), patient);
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
    public JSONResponse updateStatusOfGame(@RequestHeader("token") String token, @Valid @RequestBody StatusRequest statusRequest) {
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
    public JSONResponse sendStatisticOfGame(@RequestHeader("token") String token, @Valid @RequestBody TestStatisticToSend testStatisticToSend) {
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
    public JSONResponse getStatusOfTest(@RequestHeader("token") String token, @RequestParam UUID test_id) {
        try {
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            TestStatus testStatus = this.testStatusService.getTestStatusByTestAndPatient(this.testService.getTestById(test_id), patient);
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
    public JSONResponse updateStatusOfTest(@RequestHeader("token") String token, @Valid @RequestBody StatusRequest statusRequest) {
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
            response.put("birthDate", patient.getBirthDate());
            response.put("numberPhone", patient.getNumberPhone());
            response.put("address", patient.getAddress());

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

    @PutMapping(path = "/account/changing")
    public JSONResponse editAccountOfTutor(@RequestHeader("token") String token, @Valid @RequestBody EditPatientInfoRequest editPatientInfoRequest) {
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
            if (editPatientInfoRequest.getOrganization() != null){
                patient.setOrganization(this.organizationService.getOrganizationById(UUID.fromString(editPatientInfoRequest.getOrganization())));
            }
            if (editPatientInfoRequest.getBirthDate() != null){
                patient.setBirthDate(editPatientInfoRequest.getBirthDate());
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
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }
}
