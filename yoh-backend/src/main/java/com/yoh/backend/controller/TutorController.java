package com.yoh.backend.controller;

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
    public ArrayList<PatientListResponse> getPatients(@RequestHeader("token") String token) throws IllegalArgumentException{
        Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
        ArrayList<PatientListResponse> response = new ArrayList<PatientListResponse>();
        for (Patient patient : tutor.getPatients()){
            response.add(new PatientListResponse(patient));
        }
        return response;
    }

    @GetMapping(path = "/patients/getting/all")
    public ArrayList<PatientListResponse> getAllPatients(@RequestHeader("token") String token) throws IllegalArgumentException{
        Organization organization = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token))).getOrganization();
        ArrayList<PatientListResponse> response = new ArrayList<PatientListResponse>();
        for (Patient patient : patientService.getAllPatientsByOrganization(organization)){
            response.add(new PatientListResponse(patient));
        }
        return response;
    }

    @GetMapping(path = "/patients/getting/one")
    public PatientInfoResponse getOnePatient(@RequestHeader("token") String token, @RequestParam UUID patientUUID) throws IllegalArgumentException{
        this.userService.verifyToken(token);
        Patient patient = this.patientService.getPatientById(patientUUID);
        return new PatientInfoResponse(patient);
    }


    @PostMapping(path = "/patients/attaching")
    public BaseResponse attachPatient(@RequestHeader("token") String token, @Valid @RequestBody PatientToTutor patientToTutor) throws IllegalArgumentException{
        Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
        Patient patient = this.patientService.getPatientById(UUID.fromString(patientToTutor.getPatient()));
        tutor.getPatients().add(patient);
        return new BaseResponse("Patient was assigned", 200);
    }

    @DeleteMapping(path = "/patients/detaching")
    public BaseResponse detachPatient(@RequestHeader("token") String token, @Valid @RequestBody PatientToTutor patientToTutor) throws IllegalArgumentException{
        Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
        List<Patient> patientList = tutor.getPatients();
        for (Patient patient: patientList) {
            if (Objects.equals(patient.getId().toString(), patientToTutor.getPatient())){
                tutor.getPatients().remove(patient);
                return new BaseResponse("Patient was detached", 200);
            }
        }
        return new BaseResponse("Patient was not founded", 401);
    }

    // // [START] Games
    @PostMapping(path = "/patients/games/adding")
    public BaseResponse addGameForPatient(@RequestHeader("token") String token, @Valid @RequestBody GameToPatient gameToPatient) throws IllegalArgumentException{
        this.userService.verifyToken(token);
        Game game = this.gameService.getGameById(UUID.fromString(gameToPatient.getGame_id()));
        Patient patient = this.patientService.getPatientById(UUID.fromString(gameToPatient.getPatient_id()));
        patient.getGames().add(game);
        return new BaseResponse("Game was added", 200);
    }

    @PostMapping(path = "/patients/games/new")
    public BaseResponse newGamesForPatient(@RequestHeader("token") String token, @Valid @RequestBody GamesToPatient gamesToPatient) throws IllegalArgumentException{
        this.userService.verifyToken(token);
        Patient patient = this.patientService.getPatientById(UUID.fromString(gamesToPatient.getPatient_id()));
        List<Game> listOfGames = new ArrayList<Game>();
        for (String id : gamesToPatient.getGames_id()){
            listOfGames.add(this.gameService.getGameById(UUID.fromString(id)));
        }
        patient.setGames(listOfGames);
        return new BaseResponse("List of games was changed", 200);
    }

    @DeleteMapping(path = "/patients/games/removing")
    public BaseResponse removeGameForPatient(@RequestHeader("token") String token, @Valid @RequestBody GameToPatient gameToPatient) throws IllegalArgumentException{
        this.userService.verifyToken(token);
        Patient patient = this.patientService.getPatientById(UUID.fromString(gameToPatient.getPatient_id()));
        List<Game> listOfGames = patient.getGames();
        for (Game game: listOfGames){
            if (game.getId().toString().equals(gameToPatient.getGame_id())){
                patient.getGames().remove(this.gameService.getGameById(UUID.fromString(gameToPatient.getGame_id())));
                return new BaseResponse("Game was removed", 200);
            }
        }
        return new BaseResponse("Game was not founded", 401);
    }

    @DeleteMapping(path = "/patients/games/clear")
    public BaseResponse clearGamesForPatient(@RequestHeader("token") String token, @Valid @RequestBody PatientToTutor patientToTutor) throws IllegalArgumentException{
        this.userService.verifyToken(token);
        Patient patient = this.patientService.getPatientById(UUID.fromString(patientToTutor.getPatient()));
        patient.setGames(new ArrayList<Game>());
        return new BaseResponse("List of games was cleared", 200);
    }

    @GetMapping(path = "/patients/games/get-statistics")
    public ArrayList<GameStaticticResponse> getStatisticsForGame(@RequestHeader("token") String token, @RequestParam UUID patientUUID, @RequestParam UUID gameUUID) throws IllegalArgumentException{
        this.userService.verifyToken(token);
        Patient patient = this.patientService.getPatientById(patientUUID);
        List<GameStatistic> gameStatistics = patient.getGameStatistics();
        ArrayList<GameStaticticResponse> response = new ArrayList<>();
        for (GameStatistic statistic: gameStatistics){
            response.add(new GameStaticticResponse(statistic));
        }
        return response;
    }

    @DeleteMapping(path = "/patients/games/clear-statistics")
    public BaseResponse clearStatisticsForGame(@RequestHeader("token") String token, @Valid @RequestBody GameToPatient gameToPatient){
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
        return new BaseResponse("Stub", 200);
    }

    @PostMapping(path = "/patients/tests/adding")
    public BaseResponse addingTestForPatient(@RequestHeader("token") String token, @Valid @RequestBody TestToPatient testToPatient) throws IllegalArgumentException{
        this.userService.verifyToken(token);
        Test test = this.testService.getTestById(UUID.fromString(testToPatient.getTest_id()));
        Patient patient = this.patientService.getPatientById(UUID.fromString(testToPatient.getPatient_id()));
        patient.getTests().add(test);
        return new BaseResponse("Test was added", 200);
    }

    @PostMapping(path = "/patients/tests/new")
    public BaseResponse newTestsForPatient(@RequestHeader("token") String token, @Valid @RequestBody TestsToPatient testsToPatient) throws IllegalArgumentException{
        this.userService.verifyToken(token);
        Patient patient = this.patientService.getPatientById(UUID.fromString(testsToPatient.getPatient_id()));
        List<Test> listOfTests = new ArrayList<Test>();
        for (String id : testsToPatient.getTests_id()){
            listOfTests.add(this.testService.getTestById(UUID.fromString(id)));
        }
        patient.setTests(listOfTests);
        return new BaseResponse("List of tests was changed", 200);
    }

    @DeleteMapping(path = "/patients/tests/removing")
    public BaseResponse removeTestForPatient(@RequestHeader("token") String token, @Valid @RequestBody TestToPatient testToPatient) throws IllegalArgumentException{
        this.userService.verifyToken(token);
        Patient patient = this.patientService.getPatientById(UUID.fromString(testToPatient.getPatient_id()));
        List<Test> listOfTests = patient.getTests();
        for (Test test: listOfTests){
            if (test.getId().toString().equals(testToPatient.getTest_id())){
                patient.getTests().remove(this.testService.getTestById(UUID.fromString(testToPatient.getTest_id())));
                return new BaseResponse("Test was removed", 200);
            }
        }
        return new BaseResponse("Test was not founded", 401);
    }

    @DeleteMapping(path = "/patients/tests/clear")
    public BaseResponse clearTestsForPatients(@RequestHeader("token") String token, @Valid @RequestBody PatientToTutor patientToTutor) throws IllegalArgumentException{
        this.userService.verifyToken(token);
        Patient patient = this.patientService.getPatientById(UUID.fromString(patientToTutor.getPatient()));
        patient.setTests(new ArrayList<Test>());
        return new BaseResponse("List of test was cleared", 200);
    }

    @GetMapping(path = "/patients/tests/get-statistics")
    public ArrayList<TestStatisticResponse> getStatisticsForPatients(@RequestHeader("token") String token, @RequestParam UUID patientUUID, @RequestParam UUID testUUID) throws IllegalArgumentException{
        this.userService.verifyToken(token);
        Patient patient = this.patientService.getPatientById(patientUUID);
        List<TestStatistic> testStatistics = patient.getTestStatistics();
        ArrayList<TestStatisticResponse> response = new ArrayList<>();
        for (TestStatistic statistic: testStatistics){
            response.add(new TestStatisticResponse(statistic));
        }
        return response;
    }

    @DeleteMapping(path = "/patients/tests/clear-statistics")
    public BaseResponse deleteStatisticForPatient(@RequestHeader("token") String token, @Valid @RequestBody TestToPatient testToPatient) throws IllegalArgumentException{
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
        return new BaseResponse("Statistic for test was cleared", 200);
    }

    @GetMapping(path = "/account")
    public TutorInfoResponse accountOfTutor(@RequestHeader("token") String token) throws IllegalArgumentException{
        Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(this.userService.verifyToken(token)));
        return new TutorInfoResponse(tutor);
    }

    @PutMapping(path = "/account/changing")
    public BaseResponse editAccountOfTutor(@RequestHeader("token") String token, @Valid @RequestBody EditTutorInfoRequest editTutorInfoRequest) throws IllegalArgumentException{
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
        return new BaseResponse("Tutor account was edited", 200);
    }
    // // [END] Games

    // // [START] Tests
    // // [END] Tests

    // [END] Patients

    // [START] Account
    // [END] Account
}
