package com.yoh.backend.controller;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.yoh.backend.entity.*;
import com.yoh.backend.enums.GamePatientStatus;
import com.yoh.backend.request.*;
import com.yoh.backend.response.*;
import com.yoh.backend.service.*;
import com.yoh.backend.util.ImageUtility;
import com.yoh.backend.enums.Status;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tutor")
public class TutorController {

    @Value("${IMAGE_FOLDER}")
    private String image_folder;

    @Value("${SITE_URL}")
    private String site_url;

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

    @Autowired
    private GameStatusService gameStatusService;

    @Autowired
    private GamePatientService gamePatientService;

    @Autowired
    private GameStatisticService gameStatisticService;

    // [START] Patients

    //TODO выборка
    @GetMapping(path = "/patients/getting")
    public JSONResponse getPatients(@RequestHeader("token") String token,
                                    @RequestParam(value = "limit", required = true) Integer limit,
                                    @RequestParam(value = "start", required = true) Integer start,
                                    @RequestParam(value = "regex", required = false, defaultValue = "") String regex) {
        try {
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            ArrayList<JsonObject> patientList = new ArrayList<JsonObject>();
            JsonObject response = new JsonObject();

            List<Patient> patients = tutor.getPatients();
            //Search
            if (!Objects.equals(regex, ""))
                patients = patients.stream()
                        .filter(i -> (i.getSurname() != null && i.getSurname().toLowerCase().contains(regex.toLowerCase()))
                                || (i.getName() != null && i.getName().toLowerCase().contains(regex.toLowerCase()))
                                || (i.getSecondName() != null && i.getSecondName().toLowerCase().contains(regex.toLowerCase())))
                        .collect(Collectors.toList());

            if (patients.size() == 0) {
                response.put("previous", false);
                response.put("next", false);
                response.put("count", 0);
                response.put("results", new ArrayList<>());
                return new JSONResponse(200, response);
            }
            //Pagination
            if (start >= patients.size())
                throw new IllegalArgumentException(
                        String.format("No element at that index (%s)", start)
                );
            int lastIndex;
            if (start + limit > patients.size()){
                lastIndex = patients.size();
                response.put("next", false);
            }
            else {
                lastIndex = start + limit;
                response.put("next", true);
            }
            if (start == 0) response.put("previous", false);
            else response.put("previous", true);
            List<Patient> patientsFilteredList = new ArrayList<>();
            for (int i = start; i < lastIndex; i++){
                patientsFilteredList.add(patients.get(i));
            }
            response.put("count", patientsFilteredList.size());

            for (Patient patient : patientsFilteredList){
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
                patientInfo.put("organizationString", patient.getOrganizationString());
                patientInfo.put("image", patient.getImage());
                patientInfo.put("login", patient.getUser().getLogin());
                patientInfo.put("email", patient.getUser().getEmail());
                patientList.add(patientInfo);
            }

            response.put("results", patientList);
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @GetMapping(path = "/patients/getting/all")
    public JSONResponse getAllPatients(@RequestHeader("token") String token,
                                       @RequestParam(value = "limit", required = true) Integer limit,
                                       @RequestParam(value = "start", required = true) Integer start,
                                       @RequestParam(value = "regex", required = false, defaultValue = "") String regex) {
        try {
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Organization organization = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token))).getOrganization();
            ArrayList<JsonObject> patientList = new ArrayList<JsonObject>();
            JsonObject response = new JsonObject();
            List<Patient> patientsFilteredUnpaginatedList = patientService.getAllPatientsByOrganizationFiltered(organization, regex);

            if (patientsFilteredUnpaginatedList.size() == 0) {
                response.put("previous", false);
                response.put("next", false);
                response.put("count", 0);
                response.put("results", new ArrayList<>());
                return new JSONResponse(200, response);
            }

            //Pagination
            if (start >= patientsFilteredUnpaginatedList.size())
                throw new IllegalArgumentException(
                        String.format("No element at that index (%s)", start)
                );
            int lastIndex;
            if (start + limit > patientsFilteredUnpaginatedList.size()){
                lastIndex = patientsFilteredUnpaginatedList.size();
                response.put("next", false);
            }
            else {
                lastIndex = start + limit;
                response.put("next", true);
            }
            if (start == 0) response.put("previous", false);
            else response.put("previous", true);
            List<Patient> patientsFilteredList = new ArrayList<>();
            for (int i = start; i < lastIndex; i++){
                patientsFilteredList.add(patientsFilteredUnpaginatedList.get(i));
            }
            response.put("count", patientsFilteredList.size());

//            if (!patientsFilteredList.isEmpty()) {
            for (Patient patient : patientsFilteredList){
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
                patientInfo.put("image", patient.getImage());
                patientInfo.put("organizationString", patient.getOrganizationString());
                patientInfo.put("login", patient.getUser().getLogin());
                patientInfo.put("email", patient.getUser().getEmail());
                patientList.add(patientInfo);
            }
//            }
            response.put("results", patientList);
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @GetMapping(path = "/patients/getting/one")
    public JSONResponse getOnePatient(@RequestHeader("token") String token,
                                      @RequestParam String patientID) {
        try {
            Tutor tutorValidation = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Patient patient = this.patientService.getPatientById(UUID.fromString(patientID));
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
            response.put("image", patient.getImage());
            response.put("organization", patient.getOrganizationString());
            response.put("birthDate", patient.getBirthDate());
            response.put("numberPhone", patient.getNumberPhone());
            response.put("address", patient.getAddress());
            response.put("login", patient.getUser().getLogin());
            response.put("email", patient.getUser().getEmail());
//            if (patient.getGames() != null){
            List<GamePatient> patientGames = this.gamePatientService.getAllGamePatientsByPatient(patient);
            if (patientGames.size() != 0){
                ArrayList<JsonObject> gamesArray = new ArrayList<>();
//                for (Game game: patient.getGames()){
                for (GamePatient gamePatient: patientGames){
                    JsonObject gamesInfo = new JsonObject();
                    GameStatus gameStatus = this.gameStatusService.getGameStatusByGamePatient(gamePatient);
                    gamesInfo.put("id", gamePatient.getGame().getId().toString());
                    gamesInfo.put("name", gamePatient.getGame().getName());
                    gamesInfo.put("type", gamePatient.getGame().getType());
                    gamesInfo.put("description", gamePatient.getGame().getDescription());
                    gamesInfo.put("url", gamePatient.getGame().getUrl());
                    gamesInfo.put("image", gamePatient.getGame().getImage());
                    gamesInfo.put("assignmentDate", gameStatus.getAssignmentDate());
                    gamesInfo.put("assignedBy", gameStatus.getTutor().getId().toString());
                    gamesInfo.put("status", gameStatus.getStatus().toString());
                    gamesInfo.put("active", gamePatient.getGamePatientStatus().toString());
                    gamesArray.add(gamesInfo);
                }
                response.put("games", gamesArray);
            }
            else response.put("games", null);
            Tutor tutor = patient.getTutor();
            if (tutor != null) {
                JsonObject tutorInfo = new JsonObject();
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
                tutorInfo.put("organizationString", tutor.getOrganizationString());
                tutorInfo.put("login", tutor.getUser().getLogin());
                tutorInfo.put("email", tutor.getUser().getEmail());
                response.put("tutor", tutorInfo);
            }
            else response.put("tutor", null);

            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }


    @PostMapping(path = "/patients/attaching")
    public JSONResponse attachPatient(@RequestHeader("token") String token,
                                      @Valid @RequestBody PatientToTutor patientToTutor) {
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
    public JSONResponse detachPatient(@RequestHeader("token") String token,
                                      @Valid @RequestBody PatientToTutor patientToTutor) {

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
    public JSONResponse addGameForPatient(@RequestHeader("token") String token,
                                          @Valid @RequestBody GameToPatient gameToPatient) {
        //TODO проверить что есть
        //TODO проверить статусы
        try {
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Game game = this.gameService.getGameById(UUID.fromString(gameToPatient.getGame_id()));
            Patient patient = this.patientService.getPatientById(UUID.fromString(gameToPatient.getPatient_id()));
            GamePatient gamePatient;
            try {
                gamePatient = this.gamePatientService.getGamePatientByGameAndPatient(game, patient);
                gamePatient.setGamePatientStatus(GamePatientStatus.ACTIVE);
                this.gamePatientService.saveGamePatient(gamePatient);
            }
            catch (IllegalArgumentException ds){
                gamePatient = new GamePatient(game, patient, GamePatientStatus.ACTIVE);
                this.gamePatientService.createGamePatient(gamePatient);
            }
            GameStatus gameStatus;
            try {
                //Если игра уже была когдато назначена
                gameStatus = this.gameStatusService.getGameStatusByGamePatient(gamePatient);
                gameStatus.setTutor(tutor);
                gameStatus.setAssignmentDate(LocalDateTime.now());
                gameStatus.setStatus(Status.ASSIGNED);
            }
            catch (IllegalArgumentException ex){
                gameStatus = new GameStatus(gamePatient, tutor, LocalDateTime.now(), Status.ASSIGNED);
            }
            this.patientService.updatePatient(patient);
            this.gameService.updateGame(game);
            this.gameStatusService.createGameStatus(gameStatus);
            JsonObject response = new JsonObject();
            response.put("message", "Game was assigned");
            return new JSONResponse(200, response);
        }
        catch (Exception e){
            JsonObject exceptionResponse = new JsonObject();
            System.out.println("18");
            exceptionResponse.put("message", e.getMessage());
            System.out.println("19");
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @PostMapping(path = "/patients/games/new")
    public JSONResponse newGamesForPatient(@RequestHeader("token") String token,
                                           @Valid @RequestBody GamesToPatient gamesToPatient) {
        // TODO проверить работу
        // TODO сделать добавление статусов
        try {
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Patient patient = this.patientService.getPatientById(UUID.fromString(gamesToPatient.getPatient_id()));
            for (String id : gamesToPatient.getGames_id()) {
                Game game = this.gameService.getGameById(UUID.fromString(id));
                GamePatient gamePatient = new GamePatient(game, patient, GamePatientStatus.ACTIVE);
                this.gamePatientService.createGamePatient(gamePatient);
                GameStatus gameStatus = new GameStatus(gamePatient, tutor, LocalDateTime.now(), Status.ASSIGNED);
                this.gameStatusService.createGameStatus(gameStatus);
                this.patientService.updatePatient(patient);
            }
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
    public JSONResponse removeGameForPatient(@RequestHeader("token") String token,
                                             @Valid @RequestBody GameToPatient gameToPatient) {
        try {
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Patient patient = this.patientService.getPatientById(UUID.fromString(gameToPatient.getPatient_id()));
            Game gameToRemove = this.gameService.getGameById(UUID.fromString(gameToPatient.getGame_id()));
            GamePatient gamePatient = this.gamePatientService.getGamePatientByGameAndPatient(gameToRemove, patient);
//            if (gamePatient == null) throw new IllegalArgumentException("Sorry, but GamePatient was not found.");
            gamePatient.setGamePatientStatus(GamePatientStatus.DELETED);
            this.gamePatientService.saveGamePatient(gamePatient);
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
    public JSONResponse clearGamesForPatient(@RequestHeader("token") String token,
                                             @Valid @RequestBody PatientToTutor patientToTutor) {
        try {
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Patient patient = this.patientService.getPatientById(UUID.fromString(patientToTutor.getPatient()));
            for (GamePatient gamePatient: this.gamePatientService.getAllGamePatientsByPatient(patient)){
                gamePatient.setGamePatientStatus(GamePatientStatus.DELETED);
                this.gamePatientService.saveGamePatient(gamePatient);
            }
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

//    @GetMapping(path = "/patients/games/get-statistics")
//    public JSONResponse getStatisticsForGame(@RequestHeader("token") String token,
//                                             @RequestParam String patientID) {
//        try {
//            //TODO message
//            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
//            Patient patient = this.patientService.getPatientById(UUID.fromString(patientID));
//            ArrayList<JsonObject> gameStatisticList = new ArrayList<>();
//            for (GamePatient gamePatient : this.gamePatientService.getAllGamePatientsByPatient(patient)){
//                List<GameStatistic> statisticList = this.gameStatisticService.getGameStatisticByGamePatient(gamePatient);
//                if (statisticList != null) {
//                    for (GameStatistic statistic: statisticList) {
//                        JsonObject gameStatisticInfo = new JsonObject();
//                        gameStatisticInfo.put("id", statistic.getId().toString());
//                        gameStatisticInfo.put("patientID", statistic.getGamePatient().getPatient().getId().toString());
//                        gameStatisticInfo.put("gameID", statistic.getGamePatient().getGame().getId().toString());
//                        gameStatisticInfo.put("type", statistic.getType().toString());
//                        gameStatisticInfo.put("dateAction", statistic.getDateAction().toString());
//                        gameStatisticInfo.put("answerNumber", statistic.getAnswerNumber().toString());
//                        gameStatisticInfo.put("dateStart", statistic.getDateStart().toString());
//                        gameStatisticInfo.put("clicks", statistic.getClicks().toString());
//                        gameStatisticInfo.put("missClicks", statistic.getMissClicks().toString());
//                        gameStatisticList.add(gameStatisticInfo);
//                    }
//                }
//            }
//            JsonObject response = new JsonObject();
//            response.put("gameStatisticList", gameStatisticList);
//            return new JSONResponse(200, response);
//        }
//        catch (IllegalArgumentException e){
//            JsonObject exceptionResponse = new JsonObject();
//            exceptionResponse.put("message", e.getMessage());
//            return new JSONResponse(401, exceptionResponse);
//        }
//    }

//    @GetMapping(path = "/patients/games/get-statistic")
//    public JSONResponse getStatisticsForGame(@RequestHeader("token") String token,
//                                             @RequestParam String patientID,
//                                             @RequestParam String gameID) {
//        try {
//            //TODO message
//            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
//            Patient patient = this.patientService.getPatientById(UUID.fromString(patientID));
//            Game game = this.gameService.getGameById(UUID.fromString(gameID));
//            ArrayList<JsonObject> gameStatisticList = new ArrayList<>();
//            GamePatient gamePatient = this.gamePatientService.getGamePatientByGameAndPatient(game, patient);
//            List<GameStatistic> statisticList = this.gameStatisticService.getGameStatisticByGamePatient(gamePatient);
//            if (statisticList != null) {
//                for (GameStatistic statistic: statisticList) {
//                    JsonObject gameStatisticInfo = new JsonObject();
//                    gameStatisticInfo.put("id", statistic.getId().toString());
//                    gameStatisticInfo.put("patientID", statistic.getGamePatient().getPatient().getId().toString());
//                    gameStatisticInfo.put("gameID", statistic.getGamePatient().getGame().getId().toString());
//                    gameStatisticInfo.put("type", statistic.getType().toString());
//                    gameStatisticInfo.put("dateAction", statistic.getDateAction().toString());
//                    gameStatisticInfo.put("answerNumber", statistic.getAnswerNumber().toString());
//                    gameStatisticInfo.put("dateStart", statistic.getDateStart().toString());
//                    gameStatisticInfo.put("clicks", statistic.getClicks().toString());
//                    gameStatisticInfo.put("missClicks", statistic.getMissClicks().toString());
//                    gameStatisticList.add(gameStatisticInfo);
//                }
//            }
//            JsonObject response = new JsonObject();
//            response.put("gameStatisticList", gameStatisticList);
//            return new JSONResponse(200, response);
//        }
//        catch (IllegalArgumentException e){
//            JsonObject exceptionResponse = new JsonObject();
//            exceptionResponse.put("message", e.getMessage());
//            return new JSONResponse(401, exceptionResponse);
//        }
//    }

//    @DeleteMapping(path = "/patients/games/clear-statistics")
//    public JSONResponse clearStatisticsForGame(@RequestHeader("token") String token,
//                                               @Valid @RequestBody GameToPatient gameToPatient){
//        try {
//            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
//            Patient patient = this.patientService.getPatientById(UUID.fromString(gameToPatient.getPatient_id()));
//            Game game = this.gameService.getGameById(UUID.fromString(gameToPatient.getGame_id()));
//            GamePatient gamePatient = this.gamePatientService.getGamePatientByGameAndPatient(game, patient);
//            if (gamePatient == null) throw new IllegalArgumentException("Sorry, but GamePatient was not found.");
//            List<GameStatistic> gameStatisticList = this.gameStatisticService.getGameStatisticByGamePatient(gamePatient);
//            for (GameStatistic gameStatistic: gameStatisticList){
//                this.gameStatisticService.deleteGameStatistic(gameStatistic);
//            }
//            JsonObject response = new JsonObject();
//            response.put("message", "Game statistic was cleared");
//            return new JSONResponse(200, response);
//        }
//        catch (IllegalArgumentException e){
//            JsonObject exceptionResponse = new JsonObject();
//            exceptionResponse.put("message", e.getMessage());
//            return new JSONResponse(401, exceptionResponse);
//        }
//    }

    @GetMapping(path = "/patients/getStatusStatistic")
    public JSONResponse getPatientStatusesStatistic(@RequestHeader("token") String token,
                                                    @RequestParam String patientID) {
        try {
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(UUID.fromString(patientID)));
            if (tutor.getOrganization().getId().equals(patient.getOrganization().getId())) {
                JsonObject response = new JsonObject();
                int done = 0;
                int assigned = 0;
                int failed = 0;
                int started = 0;
                for (GamePatient gamePatient: this.gamePatientService.getAllGamePatientsByPatient(patient)){
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
            else {
                JsonObject exceptionResponse = new JsonObject();
                exceptionResponse.put("message", "Organizations must be same");
                return new JSONResponse(401, exceptionResponse);
            }
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @PostMapping(path = "/patients/tests/adding")
    public JSONResponse addingTestForPatient(@RequestHeader("token") String token,
                                             @Valid @RequestBody TestToPatient testToPatient) {
        try {
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
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
    public JSONResponse newTestsForPatient(@RequestHeader("token") String token,
                                           @Valid @RequestBody TestsToPatient testsToPatient) {
        try {
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
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
    public JSONResponse removeTestForPatient(@RequestHeader("token") String token,
                                             @Valid @RequestBody TestToPatient testToPatient) {
        try {
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
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
    public JSONResponse clearTestsForPatients(@RequestHeader("token") String token,
                                              @Valid @RequestBody PatientToTutor patientToTutor) {
        //TODO
        try {
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
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
    public JSONResponse getStatisticsForPatients(@RequestHeader("token") String token,
                                                 @RequestParam String patientID) {
        //TODO
        try {
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Patient patient = this.patientService.getPatientById(UUID.fromString(patientID));
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
    public JSONResponse deleteStatisticForPatient(@RequestHeader("token") String token,
                                                  @Valid @RequestBody TestToPatient testToPatient) {
        //TODO
        try {
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
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
            response.put("organizationString",tutor.getOrganizationString());
            response.put("login", tutor.getUser().getLogin());
            response.put("email", tutor.getUser().getEmail());
            response.put("image", tutor.getImage());
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @PutMapping(path = "/account/changing")
    public JSONResponse editAccountOfTutor(@RequestHeader("token") String token,
                                           @Valid @RequestBody EditTutorInfoRequest editTutorInfoRequest) {
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
//            if (editTutorInfoRequest.getOrganization() != null) {
//                tutor.setOrganization(this.organizationService.getOrganizationById(UUID.fromString(editTutorInfoRequest.getOrganization())));
//            }
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

    @PostMapping(path = "/account/image/add")
    public JSONResponse uploadTutorImage(@RequestHeader("token") String token,
                                         @RequestParam("image") MultipartFile file) {
        try {
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));

            String orgName = UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());

//            String orgName = tutor.getId().toString() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
//            Path filepath = Paths.get("/app/images", orgName);
//            if(new  File(filepath.toString()).exists()){
//                System.out.println("File exists");
//                new File(filepath.toString()).delete();
//            }
            if(tutor.getImage() != null){
//                new File("/app/images/" + tutor.getImage().replace(site_url + "images/", "")).delete();
                new File("/app/images/" + tutor.getImage()).delete();
                System.out.println("Old image was deleted");

            }
            File filesd = new File("/app/images", orgName);
            FileUtils.writeByteArrayToFile(filesd, file.getBytes());

//            String url = site_url + "images/" + orgName;
            tutor.setImage(orgName);
            this.tutorService.updateTutor(tutor);
            JsonObject response = new JsonObject();
            response.put("message", "Tutor account image was added");
            response.put("image", orgName);
            System.out.println();
            System.out.println(tutor.getImage());
            System.out.println();
            return new JSONResponse(200, response);
        }
        catch (Exception e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

//    //TODO Подумать над удалением
//    @PutMapping(path = "/account/image/edit")
//    public JSONResponse updateTutorImage(@RequestHeader("token") String token,
//                                         @RequestParam("image") MultipartFile file) {
//        try {
//            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
//            byte[] imageBytes = ImageUtility.compressImage(file.getBytes());
//            tutor.setImage(imageBytes);
//            this.tutorService.updateTutor(tutor);
//            JsonObject response = new JsonObject();
//            response.put("message", "Tutor account image was edited");
//            return new JSONResponse(200, response);
//        }
//        catch (Exception e){
//            JsonObject exceptionResponse = new JsonObject();
//            exceptionResponse.put("message", e.getMessage());
//            return new JSONResponse(401, exceptionResponse);
//        }
//    }

    @DeleteMapping(path = "/account/image/delete")
    public JSONResponse deleteTutorImage(@RequestHeader("token") String token) {
        try {
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
//            byte[] imageBytes = null;
//            patient.setImage(imageBytes);
            tutor.setImage(null);
            this.tutorService.updateTutor(tutor);
            JsonObject response = new JsonObject();
            response.put("message", "Tutor account image was deleted");
            return new JSONResponse(200, response);
        }
        catch (Exception e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @GetMapping(path = "/account/image")
    public JSONResponse getTutorImage(@RequestHeader("token") String token) {
        try {
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            if (tutor.getImage() != null) {
                JsonObject response = new JsonObject();
                response.put("image", tutor.getImage());
                return new JSONResponse(200, response);
            }
            else {
                JsonObject response = new JsonObject();
                response.put("message", "Tutor does not have an image");
                return new JSONResponse(200, response);
            }
        }
        catch (Exception e){
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
