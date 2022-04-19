package com.yoh.backend.controller;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.yoh.backend.entity.*;
import com.yoh.backend.request.*;
import com.yoh.backend.response.*;
import com.yoh.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/tutor")
public class TutorController {

    @Autowired
    private TutorService tutorService;

    @Autowired
    private UserService userService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private GameService gameService;

    @Autowired
    private TestService testService;

    @Autowired
    private OrganizationService organizationService;

    // [START] Patients

    @GetMapping(path = "/patients/getting")
    public JSONResponse getPatients(@RequestHeader("token") String token) {
        try {
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            ArrayList<JsonObject> patientList = new ArrayList<JsonObject>();
            for (Patient patient : tutor.getPatients()){
                JsonObject patientInfo = new JsonObject();
                patientInfo.put("id", patient.getId().toString());
                patientInfo.put("name", patient.getName());
                patientInfo.put("surname", patient.getSurname());
                if (patient.getOrganization() != null){
                    patientInfo.put("organization", patient.getOrganization().getId().toString());
                }
                else {
                    patientInfo.put("organization", null);
                }
                patientList.add(patientInfo);
            }
            JsonObject response = new JsonObject();
            response.put("patientList", patientList);
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @GetMapping(path = "/patients/getting/all")
    public JSONResponse getAllPatients(@RequestHeader("token") String token) {
        try {
            Organization organization = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token))).getOrganization();
            ArrayList<JsonObject> patientList = new ArrayList<JsonObject>();
            for (Patient patient : patientService.getAllPatientsByOrganization(organization)){
                JsonObject patientInfo = new JsonObject();
                patientInfo.put("id", patient.getId().toString());
                patientInfo.put("name", patient.getName());
                patientInfo.put("surname", patient.getSurname());
                if (patient.getOrganization() != null){
                    patientInfo.put("organization", patient.getOrganization().getId().toString());
                }
                else {
                    patientInfo.put("organization", null);
                }
                patientList.add(patientInfo);
            }
            JsonObject response = new JsonObject();
            response.put("patientList", patientList);
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @GetMapping(path = "/patients/getting/one")
    public JSONResponse getOnePatient(@RequestHeader("token") String token, @RequestParam UUID patientUUID) {
        try {
            //TODO проверка тьютора
            this.userService.verifyToken(token);
            Patient patient = this.patientService.getPatientById(patientUUID);
            JsonObject response = new JsonObject();
            response.put("id", patient.getId().toString());
            response.put("name", patient.getName());
            response.put("surname", patient.getSurname());
            response.put("secondName", patient.getSecondName());
            if (patient.getGender() != null)
                response.put("gender", patient.getGender().toString());
            else response.put("gender", null);
            if (patient.getOrganization() != null){
                response.put("organization", patient.getOrganization().getId().toString());
            }
            else {
                response.put("organization", null);
            }
            response.put("birthDate", patient.getBirthDate());
            response.put("numberPhone", patient.getNumberPhone());
            response.put("address", patient.getAddress());
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
            response.put("games", gamesArray);
            JsonObject tutorInfo = new JsonObject();
            Tutor tutor = patient.getTutor();
            tutorInfo.put("id", tutor.getId().toString());
            tutorInfo.put("name", tutor.getName());
            tutorInfo.put("surname", tutor.getSurname());
            tutorInfo.put("secondName", tutor.getSecondName());
            if (tutor.getOrganization() != null){
                tutorInfo.put("organization", tutor.getOrganization().getId().toString());
            }
            else {
                tutorInfo.put("organization", null);
            }
            response.put("tutor", tutorInfo);
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }


    @PostMapping(path = "/patients/attaching")
    public JSONResponse attachPatient(@RequestHeader("token") String token, @Valid @RequestBody PatientToTutor patientToTutor) {
        try {
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Patient patient = this.patientService.getPatientById(UUID.fromString(patientToTutor.getPatient()));
            patient.setTutor(tutor);
//            tutor.getPatients().add(patient);
            this.patientService.updatePatient(patient);
//            this.tutorService.updateTutor(tutor);
            JsonObject response = new JsonObject();
            response.put("message", "Patient was assigned");
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @DeleteMapping(path = "/patients/detaching")
    public JSONResponse detachPatient(@RequestHeader("token") String token, @Valid @RequestBody PatientToTutor patientToTutor) {

        try {
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            List<Patient> patientList = tutor.getPatients();
            for (Patient patient: patientList) {
                if (Objects.equals(patient.getId().toString(), patientToTutor.getPatient())){
//                    tutor.getPatients().remove(patient);
                    patient.setTutor(null);
                    this.patientService.updatePatient(patient);
//                    this.tutorService.updateTutor(tutor);

                    JsonObject response = new JsonObject();
                    response.put("message", "Patient was detached");
                    return new JSONResponse(200, response);
                }
            }
            JsonObject errorResponse = new JsonObject();
            errorResponse.put("message", "Patient was not founded");
            return new JSONResponse( 401, errorResponse);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    // // [START] Games
    @PostMapping(path = "/patients/games/adding")
    public JSONResponse addGameForPatient(@RequestHeader("token") String token, @Valid @RequestBody GameToPatient gameToPatient) {
        //TODO проверить что есть
        try {
            this.userService.verifyToken(token);
            Game game = this.gameService.getGameById(UUID.fromString(gameToPatient.getGame_id()));
            Patient patient = this.patientService.getPatientById(UUID.fromString(gameToPatient.getPatient_id()));
            patient.getGames().add(game);

            game.getPatient().add(patient);

            this.patientService.updatePatient(patient);

            this.gameService.updateGame(game);

            JsonObject response = new JsonObject();
            response.put("message", "Game was added");
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @PostMapping(path = "/patients/games/new")
    public JSONResponse newGamesForPatient(@RequestHeader("token") String token, @Valid @RequestBody GamesToPatient gamesToPatient) {
        // TODO проверить работу
        try {
            this.userService.verifyToken(token);
            Patient patient = this.patientService.getPatientById(UUID.fromString(gamesToPatient.getPatient_id()));
            List<Game> listOfGames = new ArrayList<Game>();
            for (String id : gamesToPatient.getGames_id()){
                listOfGames.add(this.gameService.getGameById(UUID.fromString(id)));
            }
            patient.setGames(listOfGames);
            this.patientService.updatePatient(patient);
            JsonObject response = new JsonObject();
            response.put("message", "List of games was changed");
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @DeleteMapping(path = "/patients/games/removing")
    public JSONResponse removeGameForPatient(@RequestHeader("token") String token, @Valid @RequestBody GameToPatient gameToPatient) {
        try {
            this.userService.verifyToken(token);
            Patient patient = this.patientService.getPatientById(UUID.fromString(gameToPatient.getPatient_id()));
            Game gameToRemove = this.gameService.getGameById(UUID.fromString(gameToPatient.getGame_id()));
            patient.getGames().remove(gameToRemove);
            gameToRemove.getPatient().remove(patient);

//            for (Patient patient1: gameToRemove.getPatient()){
//                if(patient1.getId() == patient.getId()) {
//                    patient1.getGames().remove(gameToRemove);
//                    this.patientService.updatePatient(patient1);
//                }
//            }

//            patient.getGames().remove(gameToRemove);

            this.patientService.updatePatient(patient);
//
//            gameToRemove.getPatient().remove(patient);
            this.gameService.updateGame(gameToRemove);
//            List<Game> listOfGames = patient.getGames();
//            listOfGames.remove(game);
//            patient.setGames(listOfGames);
//
//            for (Game game: listOfGames){
//                if (game.getId().toString().equals(gameToPatient.getGame_id())){
//                    patient.getGames().remove(this.gameService.getGameById(UUID.fromString(gameToPatient.getGame_id())));
//                    this.patientService.updatePatient(patient);
//                    JsonObject response = new JsonObject();
//                    response.put("message", "Game was removed");
//                    return new JSONResponse(200, response);
//                }
//            }
            JsonObject errorResponse = new JsonObject();
            errorResponse.put("message", "Game was detached");
            return new JSONResponse(200, errorResponse);
        }
        catch (Exception e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @DeleteMapping(path = "/patients/games/clear")
    public JSONResponse clearGamesForPatient(@RequestHeader("token") String token, @Valid @RequestBody PatientToTutor patientToTutor) {
        try {
            this.userService.verifyToken(token);
            Patient patient = this.patientService.getPatientById(UUID.fromString(patientToTutor.getPatient()));
            patient.setGames(new ArrayList<Game>());
            this.patientService.updatePatient(patient);
            JsonObject response = new JsonObject();
            response.put("message", "List of games was cleared");
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @GetMapping(path = "/patients/games/get-statistics")
    public JSONResponse getStatisticsForGame(@RequestHeader("token") String token, @RequestParam UUID patientUUID, @RequestParam UUID gameUUID) {
        try {
            this.userService.verifyToken(token);
            Patient patient = this.patientService.getPatientById(patientUUID);
            List<GameStatistic> gameStatistics = patient.getGameStatistics();
            ArrayList<JsonObject> gameStatisticList = new ArrayList<>();
            for (GameStatistic statistic: gameStatistics){
                JsonObject gameStatisticInfo = new JsonObject();
                gameStatisticInfo.put("id", statistic.getId().toString());
                gameStatisticInfo.put("patientID", statistic.getPatient().getId().toString());
                gameStatisticInfo.put("gameID", statistic.getGame().getId().toString());
                gameStatisticInfo.put("type", statistic.getType().toString());
                gameStatisticInfo.put("dateAction", statistic.getDateAction().toString());
                gameStatisticInfo.put("answerNumber", statistic.getDateAction().toString());
//                gameStatisticInfo.put("message", statistic.getMessage());
                gameStatisticList.add(gameStatisticInfo);
            }
            JsonObject response = new JsonObject();
            response.put("gameStatisticList", gameStatisticList);
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @DeleteMapping(path = "/patients/games/clear-statistics")
    public JSONResponse clearStatisticsForGame(@RequestHeader("token") String token, @Valid @RequestBody GameToPatient gameToPatient){
        try {
            this.userService.verifyToken(token);
            Patient patient = this.patientService.getPatientById(UUID.fromString(gameToPatient.getPatient_id()));
            ArrayList<GameStatistic> newGameStatistic = new ArrayList<>();
            UUID gameToSearch = UUID.fromString(gameToPatient.getGame_id());
            for (GameStatistic gameStatistic : patient.getGameStatistics()){
                if (gameStatistic.getGame().getId() != gameToSearch){
                    newGameStatistic.add(gameStatistic);
                }
            }
            patient.setGameStatistics(newGameStatistic);
            this.patientService.updatePatient(patient);
            JsonObject response = new JsonObject();
            response.put("message", "Game statistic was cleared");
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @PostMapping(path = "/patients/tests/adding")
    public JSONResponse addingTestForPatient(@RequestHeader("token") String token, @Valid @RequestBody TestToPatient testToPatient) {
        try {
            this.userService.verifyToken(token);
            Test test = this.testService.getTestById(UUID.fromString(testToPatient.getTest_id()));
            Patient patient = this.patientService.getPatientById(UUID.fromString(testToPatient.getPatient_id()));
            patient.getTests().add(test);
            this.patientService.updatePatient(patient);
            JsonObject response = new JsonObject();
            response.put("message", "Test was added");
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @PostMapping(path = "/patients/tests/new")
    public JSONResponse newTestsForPatient(@RequestHeader("token") String token, @Valid @RequestBody TestsToPatient testsToPatient) {
        try {
            this.userService.verifyToken(token);
            Patient patient = this.patientService.getPatientById(UUID.fromString(testsToPatient.getPatient_id()));
            List<Test> listOfTests = new ArrayList<Test>();
            for (String id : testsToPatient.getTests_id()){
                listOfTests.add(this.testService.getTestById(UUID.fromString(id)));
            }
            patient.setTests(listOfTests);
            this.patientService.updatePatient(patient);
            JsonObject response = new JsonObject();
            response.put("message", "List of tests was changed");
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @DeleteMapping(path = "/patients/tests/removing")
    public JSONResponse removeTestForPatient(@RequestHeader("token") String token, @Valid @RequestBody TestToPatient testToPatient) {
        try {
            this.userService.verifyToken(token);
            Patient patient = this.patientService.getPatientById(UUID.fromString(testToPatient.getPatient_id()));
            List<Test> listOfTests = patient.getTests();
            for (Test test: listOfTests){
                if (test.getId().toString().equals(testToPatient.getTest_id())){
                    patient.getTests().remove(this.testService.getTestById(UUID.fromString(testToPatient.getTest_id())));
                    this.patientService.updatePatient(patient);
                    JsonObject response = new JsonObject();
                    response.put("message", "Test was removed");
                    return new JSONResponse(200, response);
                }
            }
            JsonObject errorResponse = new JsonObject();
            errorResponse.put("message", "Test was not founded");
            return new JSONResponse(401, errorResponse);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @DeleteMapping(path = "/patients/tests/clear")
    public JSONResponse clearTestsForPatients(@RequestHeader("token") String token, @Valid @RequestBody PatientToTutor patientToTutor) {
        //TODO
        try {
            this.userService.verifyToken(token);
            Patient patient = this.patientService.getPatientById(UUID.fromString(patientToTutor.getPatient()));
            patient.setTests(new ArrayList<Test>());
            this.patientService.updatePatient(patient);
            JsonObject response = new JsonObject();
            response.put("message", "List of test was cleared");
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @GetMapping(path = "/patients/tests/get-statistics")
    public JSONResponse getStatisticsForPatients(@RequestHeader("token") String token, @RequestParam UUID patientUUID, @RequestParam UUID testUUID) {
        //TODO
        try {
            this.userService.verifyToken(token);
            Patient patient = this.patientService.getPatientById(patientUUID);
            List<TestStatistic> testStatistics = patient.getTestStatistics();
            ArrayList<JsonObject> testStatisticList = new ArrayList<>();
            for (TestStatistic statistic: testStatistics){
                JsonObject testStatisticInfo = new JsonObject();
                testStatisticInfo.put("id", statistic.getId().toString());
                testStatisticInfo.put("patientID", statistic.getPatient().getId().toString());
                testStatisticInfo.put("gameID", statistic.getTest().getId().toString());
                testStatisticInfo.put("type", statistic.getType().toString());
                testStatisticInfo.put("dateAction", statistic.getDateAction().toString());
                testStatisticInfo.put("message", statistic.getMessage());
                testStatisticList.add(testStatisticInfo);
            }
            JsonObject response = new JsonObject();
            response.put("testStatisticList", testStatisticList);
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @DeleteMapping(path = "/patients/tests/clear-statistics")
    public JSONResponse deleteStatisticForPatient(@RequestHeader("token") String token, @Valid @RequestBody TestToPatient testToPatient) {
        //TODO
        try {
            this.userService.verifyToken(token);
            Patient patient = this.patientService.getPatientById(UUID.fromString(testToPatient.getPatient_id()));
            ArrayList<TestStatistic> newTestStatistic = new ArrayList<>();
            UUID testToSearch = UUID.fromString(testToPatient.getTest_id());
            for (TestStatistic testStatistic : patient.getTestStatistics()){
                if (testStatistic.getTest().getId() != testToSearch){
                    newTestStatistic.add(testStatistic);
                }
            }
            patient.setTestStatistics(newTestStatistic);
            this.patientService.updatePatient(patient);
            JsonObject response = new JsonObject();
            response.put("message", "Statistic for test was cleared");
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @GetMapping(path = "/account")
    public JSONResponse accountOfTutor(@RequestHeader("token") String token) {
        try {
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            JsonObject response = new JsonObject();
            response.put("id", tutor.getId().toString());
            response.put("name", tutor.getName());
            response.put("surname", tutor.getSurname());
            response.put("secondName", tutor.getSecondName());
            if (tutor.getOrganization() != null){
                response.put("organization", tutor.getOrganization().getId().toString());
            }
            else response.put("organization", null);
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @PutMapping(path = "/account/changing")
    public JSONResponse editAccountOfTutor(@RequestHeader("token") String token, @Valid @RequestBody EditTutorInfoRequest editTutorInfoRequest) {
        try {
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            if (editTutorInfoRequest.getName() != null) {
                tutor.setName(editTutorInfoRequest.getName());
            }
            if (editTutorInfoRequest.getSurname() != null) {
                tutor.setSurname(editTutorInfoRequest.getSurname());
            }
            if (editTutorInfoRequest.getSecondName() != null) {
                tutor.setSecondName(editTutorInfoRequest.getSecondName());
            }
            if (editTutorInfoRequest.getOrganization() != null) {
                tutor.setOrganization(this.organizationService.getOrganizationById(UUID.fromString(editTutorInfoRequest.getOrganization())));
            }
            this.tutorService.updateTutor(tutor);
            JsonObject response = new JsonObject();
            response.put("message", "Tutor account was edited");
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }
    // // [END] Games

    // // [START] Tests
    // // [END] Tests

    // [END] Patients

    // [START] Account
    // [END] Account
}
