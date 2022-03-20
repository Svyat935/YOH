package com.yoh.backend.controller;

import com.yoh.backend.entity.*;
import com.yoh.backend.enums.Gender;
import com.yoh.backend.request.EditPatientInfoRequest;
import com.yoh.backend.request.GameStatisticToSend;
import com.yoh.backend.request.StatusRequest;
import com.yoh.backend.request.TestStatisticToSend;
import com.yoh.backend.response.BaseResponse;
import com.yoh.backend.response.GameStatusResponse;
import com.yoh.backend.response.PatientInfoResponse;
import com.yoh.backend.response.TestStatusResponse;
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
    public BaseResponse getAllGames(@RequestHeader("token") String token) throws IllegalArgumentException{
        // TODO Уточнить как игры возвращаться будут.
        Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
        return new BaseResponse("Stub", 200);
    }
    @PostMapping(path = "/games/statistics/sending")
    public BaseResponse sendGameStatistic(@RequestHeader("token") String token, @Valid @RequestBody GameStatisticToSend gameStatisticToSend) throws IllegalArgumentException{
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
        return new BaseResponse("GameStatistic was added", 200);
    }

    @GetMapping(path = "/games/status")
    public GameStatusResponse getStatusOfGame(@RequestHeader("token") String token, @RequestParam UUID game_id) throws IllegalArgumentException{
        Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
        GameStatus gameStatus = this.gameStatusService.getGameStatusByGameAndPatient(this.gameService.getGameById(game_id), patient);
        return new GameStatusResponse(gameStatus);
    }

    @PutMapping(path = "/games/status/update")
    public BaseResponse updateStatusOfGame(@RequestHeader("token") String token, @Valid @RequestBody StatusRequest statusRequest) throws IllegalArgumentException{
        // TODO Сделать это когда-нибудь
        Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));

        return new BaseResponse("Status of the game was updated", 200);
    }

    @GetMapping(path = "/tests/getting")
    public BaseResponse getAllTests(@RequestHeader("token") String token) throws IllegalArgumentException{
        // TODO Уточнить как тесты возвращаться будут
        Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));

        return new BaseResponse("Stub", 200);
    }

    @PostMapping(path = "/tests/statistics/sending")
    public BaseResponse sendStatisticOfGame(@RequestHeader("token") String token, @Valid @RequestBody TestStatisticToSend testStatisticToSend) throws IllegalArgumentException{
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
        return new BaseResponse("TestStatistic was added", 200);
    }

    @GetMapping(path = "/tests/status")
    public TestStatusResponse getStatusOfTest(@RequestHeader("token") String token, @RequestParam UUID test_id) throws IllegalArgumentException{
        Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
        TestStatus testStatus = this.testStatusService.getTestStatusByTestAndPatient(this.testService.getTestById(test_id), patient);
        return new TestStatusResponse(testStatus);
    }

    @PutMapping(path = "/tests/status/update")
    public BaseResponse updateStatusOfTest(@RequestHeader("token") String token, @Valid @RequestBody StatusRequest statusRequest) throws IllegalArgumentException{
        // TODO Сделать это когда-нибудь
        Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));

        return new BaseResponse("Status of the test was updated", 200);
    }

    @GetMapping(path = "/account")
    public PatientInfoResponse getAccountInfo(@RequestHeader("token") String token) throws IllegalArgumentException{
        Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(this.userService.verifyToken(token)));
        return new PatientInfoResponse(patient);
    }

    @PutMapping(path = "/account/changing")
    public BaseResponse editAccountOfTutor(@RequestHeader("token") String token, @Valid @RequestBody EditPatientInfoRequest editPatientInfoRequest) throws IllegalArgumentException{
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
        return new BaseResponse("Patient account was edited", 200);
    }
}
