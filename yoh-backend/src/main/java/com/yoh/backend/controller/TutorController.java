package com.yoh.backend.controller;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.yoh.backend.entity.*;
import com.yoh.backend.enums.GameActiveStatus;
import com.yoh.backend.enums.GamePatientStatus;
import com.yoh.backend.request.*;
import com.yoh.backend.service.*;
import com.yoh.backend.enums.Status;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
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
    private GamePatientService gamePatientService;

    @Autowired
    private GameStatisticService gameStatisticService;

    @Autowired
    private StartedGameService startedGameService;

    // [START] Patients

    @GetMapping(path = "/patients/getting")
    public ResponseEntity<JsonObject> getPatients(@RequestHeader("token") String token,
                                                  @RequestParam(value = "limit", required = true) Integer limit,
                                                  @RequestParam(value = "start", required = true) Integer start,
                                                  @RequestParam(value = "regex", required = false, defaultValue = "") String regex,
                                                  @RequestParam(value = "order", required = false, defaultValue = "1") String order) {
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

            if (order.equals("1"))
                patients = patients.stream().sorted(Patient::compareTo).collect(Collectors.toList());
            if (order.equals("-1"))
                patients = patients.stream().sorted(Collections.reverseOrder()).collect(Collectors.toList());

            if (patients.size() == 0) {
                response.put("previous", false);
                response.put("next", false);
                response.put("count", 0);
                response.put("size", 0);
                response.put("results", new ArrayList<>());
                return new ResponseEntity<>(response, HttpStatus.OK);
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
            response.put("size", patients.size());

            for (Patient patient : patientsFilteredList){
                JsonObject patientInfo = new JsonObject();
                patientInfo.put("id", patient.getId().toString());
                patientInfo.put("name", patient.getName());
                patientInfo.put("surname", patient.getSurname());
                if (patient.getOrganization() != null){
                    patientInfo.put("oganizationr", patient.getOrganization().getId().toString());
                }
                else {
                    patientInfo.put("organization", null);
                }
                JsonObject statusInfo = new JsonObject();
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
                statusInfo.put("Done", done);
                statusInfo.put("Assigned", assigned);
                statusInfo.put("Failed", failed);
                statusInfo.put("Started", started);
                patientInfo.put("statusInfo", statusInfo);

                if (patient.getOrganization() != null)
                    patientInfo.put("organizationString", patient.getOrganization().getName());
                else
                    patientInfo.put("organizationString", null);
                patientInfo.put("image", patient.getImage());
                patientInfo.put("login", patient.getUser().getLogin());
                patientInfo.put("email", patient.getUser().getEmail());
                patientList.add(patientInfo);
            }

            response.put("results", patientList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/patients/getting/all")
    public ResponseEntity<JsonObject> getAllPatients(@RequestHeader("token") String token,
                                       @RequestParam(value = "limit", required = true) Integer limit,
                                       @RequestParam(value = "start", required = true) Integer start,
                                       @RequestParam(value = "regex", required = false, defaultValue = "") String regex,
                                       @RequestParam(value = "order", required = false, defaultValue = "1") String order) {
        try {
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Organization organization = tutor.getOrganization();
            ArrayList<JsonObject> patientInfoList = new ArrayList<JsonObject>();
            JsonObject response = new JsonObject();
            int listCount = this.patientService.getAllPatientsByOrganizationFilteredCount(organization, regex);
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

            List<Patient> patientList = this.patientService.getAllPatientsByOrganizationFilteredPaginated(organization, regex, order, start, limit);
            if (start == 0)
                response.put("previous", false);
            else response.put("previous", true);

            if (start + limit > listCount)
                response.put("next", false);
            else response.put("next", true);

            response.put("count", patientList.size());
            response.put("size", listCount);
            for (Patient patient : patientList){
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
                if (patient.getOrganization() != null)
                    patientInfo.put("organizationString", patient.getOrganization().getName());
                else
                    patientInfo.put("organizationString", null);
                patientInfo.put("login", patient.getUser().getLogin());
                patientInfo.put("email", patient.getUser().getEmail());
                patientInfo.put("dateRegistration", patient.getUser().getDateRegistration().toString());
                patientInfoList.add(patientInfo);
            }
//            }
            response.put("results", patientInfoList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/patients/getting/one")
    public ResponseEntity<JsonObject> getOnePatient(@RequestHeader("token") String token,
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
            if (patient.getOrganization() != null)
                response.put("organization", patient.getOrganization().getName());
            else
                response.put("organization", null);
            response.put("birthDate", patient.getBirthDate());
            response.put("numberPhone", patient.getNumberPhone());
            response.put("address", patient.getAddress());
            response.put("login", patient.getUser().getLogin());
            response.put("email", patient.getUser().getEmail());
            Tutor tutor = patient.getTutor();
            if (tutor != null) {
                JsonObject tutorInfo = new JsonObject();
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
            else response.put("tutor", null);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping(path = "/patients/getting/one/games")
    public ResponseEntity<JsonObject> getOnePatientGames(@RequestHeader("token") String token,
                                           @RequestParam String patientID,
                                           @RequestParam(value = "limit", required = true) Integer limit,
                                           @RequestParam(value = "start", required = true) Integer start,
                                           @RequestParam(value = "regex", required = false, defaultValue = "") String regex,
                                           @RequestParam(value = "typeRegex", required = false, defaultValue = "") String typeRegex,
                                           @RequestParam(value = "order", required = false, defaultValue = "1") String order) {
        try {
            Tutor tutorValidation = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Patient patient = this.patientService.getPatientById(UUID.fromString(patientID));
            ArrayList<JsonObject> gamesArray = new ArrayList<>();
            JsonObject response = new JsonObject();
            int listCount = this.gamePatientService.getAllGamePatientsByPatientCount(patient, typeRegex, regex);
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

            List<GamePatient> gamePatientList = this.gamePatientService.getAllGamePatientsByPatientOrderedPaginated(patient, order, typeRegex, regex, start, limit);
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
                gamesInfo.put("gamePatientId", gamePatient.getId().toString());
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


    @PostMapping(path = "/patients/attaching")
    public ResponseEntity<JsonObject> attachPatient(@RequestHeader("token") String token,
                                      @Valid @RequestBody PatientToTutor patientToTutor) {
        try {
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Patient patient = this.patientService.getPatientById(UUID.fromString(patientToTutor.getPatient()));
            if (patient.getTutor() == null){
                patient.setTutor(tutor);
                this.patientService.updatePatient(patient);

            }
            else throw new IllegalArgumentException("Patient is already assigned");
            JsonObject response = new JsonObject();
            response.put("message", "Patient was assigned");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "/patients/detaching")
    public ResponseEntity<JsonObject> detachPatient(@RequestHeader("token") String token,
                                      @Valid @RequestBody PatientToTutor patientToTutor) {

        try {
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Patient patient = this.patientService.getPatientById(UUID.fromString(patientToTutor.getPatient()));
            if (patient.getTutor().getId().equals(tutor.getId())){
                patient.setTutor(null);
                this.patientService.updatePatient(patient);
                JsonObject response = new JsonObject();
                response.put("message", "Patient was assigned");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            else throw new IllegalArgumentException("Patient is not assigned to that tutor");
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }

    // // [START] Games
    @PostMapping(path = "/patients/games/adding")
    public ResponseEntity<JsonObject> addGameForPatient(@RequestHeader("token") String token,
                                          @Valid @RequestBody GameToPatient gameToPatient) {
        try {
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Game game = this.gameService.getGameById(UUID.fromString(gameToPatient.getGame_id()));
            if (!game.getGameActiveStatus().equals(GameActiveStatus.ACTIVE))
                throw new IllegalArgumentException("Game is not active");
            Patient patient = this.patientService.getPatientById(UUID.fromString(gameToPatient.getPatient_id()));
            GamePatient gamePatient;
            try {
                gamePatient = this.gamePatientService.getGamePatientByGameAndPatient(game, patient);
                gamePatient.setGamePatientStatus(GamePatientStatus.ACTIVE);
                gamePatient.setStatus(Status.ASSIGNED);
                gamePatient.setAssignmentDate(LocalDateTime.now());
                gamePatient.setTutor(tutor);
                this.gamePatientService.saveGamePatient(gamePatient);
            }
            catch (IllegalArgumentException ds){
                gamePatient = new GamePatient(game, patient, tutor, LocalDateTime.now(), GamePatientStatus.ACTIVE, Status.ASSIGNED);
                this.gamePatientService.createGamePatient(gamePatient);
            }
            JsonObject response = new JsonObject();
            response.put("message", "Game was assigned");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/patients/games/new")
    public ResponseEntity<JsonObject> newGamesForPatient(@RequestHeader("token") String token,
                                           @Valid @RequestBody GamesToPatient gamesToPatient) {
        try {
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Patient patient = this.patientService.getPatientById(UUID.fromString(gamesToPatient.getPatient_id()));
            for (String id : gamesToPatient.getGames_id()) {
                Game game = this.gameService.getGameById(UUID.fromString(id));
                GamePatient gamePatient;
                try {
                    gamePatient = this.gamePatientService.getGamePatientByGameAndPatient(game, patient);
                    gamePatient.setGamePatientStatus(GamePatientStatus.ACTIVE);
                    gamePatient.setStatus(Status.ASSIGNED);
                    gamePatient.setAssignmentDate(LocalDateTime.now());
                    gamePatient.setTutor(tutor);
                    this.gamePatientService.saveGamePatient(gamePatient);
                }
                catch (IllegalArgumentException ds){
                    gamePatient = new GamePatient(game, patient, tutor, LocalDateTime.now(), GamePatientStatus.ACTIVE, Status.ASSIGNED);
                    this.gamePatientService.createGamePatient(gamePatient);
                }
            }
            JsonObject response = new JsonObject();
            response.put("message", "List of games was changed");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "/patients/games/removing")
    public ResponseEntity<JsonObject> removeGameForPatient(@RequestHeader("token") String token,
                                             @Valid @RequestBody GameToPatient gameToPatient) {
        try {
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Patient patient = this.patientService.getPatientById(UUID.fromString(gameToPatient.getPatient_id()));
            Game gameToRemove = this.gameService.getGameById(UUID.fromString(gameToPatient.getGame_id()));
            GamePatient gamePatient = this.gamePatientService.getGamePatientByGameAndPatient(gameToRemove, patient);
            gamePatient.setGamePatientStatus(GamePatientStatus.DELETED);
            this.gamePatientService.saveGamePatient(gamePatient);
            JsonObject response = new JsonObject();
            response.put("message", "Game was detached");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "/patients/games/clear")
    public ResponseEntity<JsonObject> clearGamesForPatient(@RequestHeader("token") String token,
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
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/patients/games/statistic")
    public ResponseEntity<JsonObject> getStatisticsForGame(@RequestHeader("token") String token,
                                             @RequestParam String patientID,
                                             @RequestParam String gameID) {
        try {
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Patient patient = this.patientService.getPatientById(UUID.fromString(patientID));
            Game game = this.gameService.getGameById(UUID.fromString(gameID));
            ArrayList<JsonObject> gameStatisticList = new ArrayList<>();
            GamePatient gamePatient = this.gamePatientService.getGamePatientByGameAndPatient(game, patient);
            List<StartedGame> startedGames = this.startedGameService.getStartedGamesByGamePatient(gamePatient);
            for (StartedGame startedGame: startedGames) {
                GameStatistic gameStatistic = this.gameStatisticService.getGameStatisticByStartedGame(startedGame);
                if (gameStatistic != null){
                    JsonObject gameStatisticInfo = new JsonObject();
                    gameStatisticInfo.put("id", gameStatistic.getId().toString());
                    gameStatisticInfo.put("game", gameStatistic.getStartedGame().getGamePatient().getGame().getId());
                    gameStatisticInfo.put("patient", gameStatistic.getStartedGame().getGamePatient().getPatient().getId());
                    gameStatisticInfo.put("level", gameStatistic.getLevel());
                    gameStatisticInfo.put("levelName", gameStatistic.getLevelName());
                    gameStatisticInfo.put("dateStart", gameStatistic.getDateStart().toString());
                    if (gameStatistic.getDateEnd() != null) gameStatisticInfo.put("dateEnd", gameStatistic.getDateEnd());
                    else gameStatisticInfo.put("dateEnd", null);
                    gameStatisticInfo.put("type", gameStatistic.getType());
                    gameStatisticInfo.put("clicks", gameStatistic.getClicks());
                    gameStatisticInfo.put("missClicks", gameStatistic.getMissClicks());
                    gameStatisticInfo.put("details", gameStatistic.getDetails());
                    gameStatisticList.add(gameStatisticInfo);
                }
            }
            JsonObject response = new JsonObject();
            response.put("gameStatisticList", gameStatisticList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping(path = "/patients/getStatusStatistic")
    public ResponseEntity<JsonObject> getPatientStatusesStatistic(@RequestHeader("token") String token,
                                                    @RequestParam String patientID) {
        try {
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Patient patient = this.patientService.getPatientById(UUID.fromString(patientID));
            if (tutor.getOrganization().getId().equals(patient.getOrganization().getId())) {
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
            else {
                JsonObject exceptionResponse = new JsonObject();
                exceptionResponse.put("message", "Organizations must be same");
                return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
            }
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping(path = "/patients/tests/get-statistics")
    public ResponseEntity<JsonObject> getStatisticsForPatients(@RequestHeader("token") String token,
                                                 @RequestParam String patientID) {
        try {
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
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "/patients/tests/clear-statistics")
    public ResponseEntity<JsonObject> deleteStatisticForPatient(@RequestHeader("token") String token,
                                                  @Valid @RequestBody TestToPatient testToPatient) {
        try {
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
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/account")
    public ResponseEntity<JsonObject> accountOfTutor(@RequestHeader("token") String token) {
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
            if (tutor.getOrganization() != null) {
                response.put("organization", tutor.getOrganization().getId().toString());
                response.put("organizationString", tutor.getOrganization().getName());
            }
            else {
                response.put("organization", null);
                response.put("organizationString", null);
            }
            response.put("login", tutor.getUser().getLogin());
            response.put("email", tutor.getUser().getEmail());
            response.put("image", tutor.getImage());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/account/changing")
    public ResponseEntity<JsonObject> editAccountOfTutor(@RequestHeader("token") String token,
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
            this.tutorService.updateTutor(tutor);
            JsonObject response = new JsonObject();
            response.put("message", "Tutor account was edited");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/account/image/add")
    public ResponseEntity<JsonObject> uploadTutorImage(@RequestHeader("token") String token,
                                         @RequestParam("image") MultipartFile file) {
        try {
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));

            String orgName = UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());

            if(tutor.getImage() != null){
                new File("/app/images/" + tutor.getImage()).delete();
                System.out.println("Old image was deleted");

            }
            File filesd = new File("/app/images", orgName);
            FileUtils.writeByteArrayToFile(filesd, file.getBytes());

            tutor.setImage(orgName);
            this.tutorService.updateTutor(tutor);
            JsonObject response = new JsonObject();
            response.put("message", "Tutor account image was added");
            response.put("image", orgName);
            System.out.println();
            System.out.println(tutor.getImage());
            System.out.println();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping(path = "/account/image/delete")
    public ResponseEntity<JsonObject> deleteTutorImage(@RequestHeader("token") String token) {
        try {
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            if (tutor == null) throw new IllegalArgumentException("Patient was not found");
            if(tutor.getImage() != null){
                new File("/app/images/" + tutor.getImage()).delete();
                System.out.println("Old image was deleted");
                tutor.setImage(null);
                this.tutorService.updateTutor(tutor);
                JsonObject response = new JsonObject();
                response.put("message", "Tutor account image was deleted");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            else {
                JsonObject response = new JsonObject();
                response.put("message", "Tutor image is already deleted");
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
    public ResponseEntity<JsonObject> getTutorImage(@RequestHeader("token") String token) {
        try {
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            if (tutor.getImage() != null) {
                JsonObject response = new JsonObject();
                response.put("image", tutor.getImage());
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            else {
                JsonObject response = new JsonObject();
                response.put("message", "Tutor does not have an image");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
        catch (Exception e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
