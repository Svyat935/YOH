package com.yoh.backend.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.yoh.backend.entity.*;
import com.yoh.backend.enums.GameActiveStatus;
import com.yoh.backend.enums.Gender;
import com.yoh.backend.request.*;
//import com.yoh.backend.response.JSONResponse;
import com.yoh.backend.response.UserInfoResponse;
import com.yoh.backend.service.*;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.*;


@RestController
@RequestMapping("/admins")
public class AdminController {
    @Value("${GAMES_FOLDER}")
    private String games_folder;

    @Value("${WRAPPER}")
    private String wrapper;

    @Value("${IMAGE_FOLDER}")
    private String image_folder;

    @Value("${SITE_URL}")
    private String site_url;

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private ResearcherService researcherService;

    @Autowired
    private TutorService tutorService;

    @Autowired
    private GameService gameService;

    @GetMapping(path = "/users/all")
    public JsonObject getUsers(@RequestHeader("token") String token,
                                 @RequestParam(value = "limit", required = true) Integer limit,
                                 @RequestParam(value = "start", required = true) Integer start,
                                 @RequestParam(value = "role", required = false, defaultValue = "-1") String role,
                                 @RequestParam(value = "regex", required = false, defaultValue = "") String regex,
                                 @RequestParam(value = "order", required = false, defaultValue = "") String order) {
        try {
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            JsonObject response = new JsonObject();
            List<UserInfoResponse> responseList = new ArrayList<>();
            int listCount = this.userService.getAllUsersByAdminCount(Integer.parseInt(role), regex);
            if (listCount == 0) {
                response.put("previous", false);
                response.put("next", false);
                response.put("count", 0);
                response.put("size", 0);
                response.put("results", new ArrayList<>());
                return response;
            }
            if (start >= listCount)
                throw new IllegalArgumentException(String.format("No element at that index (%s)", start));

            List<User> userList = this.userService.getAllUsersByAdminPaginated(Integer.parseInt(role), regex, order, start, limit);

            if (start == 0)
                response.put("previous", false);
            else response.put("previous", true);

            if (start + limit > listCount)
                response.put("next", false);
            else response.put("next", true);

            response.put("count", userList.size());
            response.put("size", listCount);
            for (User user: userList){
                responseList.add(new UserInfoResponse(user));
            }
            response.put("results", responseList);
            return response;
//            List<User> userList = this.userService.getAllUsersByAdmin(Integer.parseInt(role), regex, order);
//            if (userList.size() == 0) {
////                JsonObject response = new JsonObject();
//                response.put("previous", false);
//                response.put("next", false);
//                response.put("count", 0);
//                response.put("size", 0);
//                response.put("results", new ArrayList<>());
//                return new JSONResponse(200, response);
//            }
//            //Pagination
//            if (start >= userList.size())
//                throw new IllegalArgumentException(
//                        String.format("No element at that index (%s)", start)
//                );
//            int lastIndex;
//            if (start + limit > userList.size()){
//                lastIndex = userList.size();
//                response.put("next", false);
//            }
//            else {
//                lastIndex = start + limit;
//                response.put("next", true);
//            }
//            if (start == 0) response.put("previous", false);
//            else response.put("previous", true);
//            List<User> paginatedUserList = new ArrayList<>();
//            for (int i = start; i < lastIndex; i++){
//                paginatedUserList.add(userList.get(i));
//            }
//            response.put("count", paginatedUserList.size());
//            response.put("size", paginatedUserList.size());
//
//            for (User user: paginatedUserList) {
//                responseList.add(new UserInfoResponse(user));
//            }
//            response.put("userList", this.userService.getAllUsers());
//            response.put("results", responseList);
//            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return exceptionResponse;
        }
    }

    @GetMapping(path = "/users/get")
    public JsonObject getUserInfo(@RequestHeader("token") String token,
                                    @RequestParam String id) {
        try {
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            User user = this.userService.getUserById(UUID.fromString(id));
            switch (user.getRole()) {
                case 1 -> {
                    //patient
                    Patient patient = this.patientService.getPatientByUser(user);
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

                    }
                    else {
                        response.put("organization", null);
                        response.put("organizationString", null);
                    }
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
                    if (tutor != null) {
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
                    return response;
                }
                case 3 -> {
                    //tutor
                    Tutor tutor = this.tutorService.getTutorByUser(user);
                    JsonObject response = new JsonObject();
                    response.put("id", tutor.getId().toString());
                    response.put("name", tutor.getName());
                    response.put("surname", tutor.getSurname());
                    response.put("secondName", tutor.getSecondName());
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
                    return response;
                }
            }
            JsonObject genericResponse = new JsonObject();
            genericResponse.put("message", "User was not founded");
            return genericResponse;
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return exceptionResponse;
        }
    }

    @GetMapping(path = "/users/tutor/image")
    public JsonObject getTutorImage(@RequestHeader("token") String token,
                                      @RequestParam String userID) {
        try {
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(UUID.fromString(userID)));
            if (tutor.getImage() != null) {
                JsonObject response = new JsonObject();
                response.put("image", tutor.getImage());
                return response;
            }
            JsonObject response = new JsonObject();
            response.put("message", "Tutor does not have an image");
            return response;
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return exceptionResponse;
        }
    }

    @GetMapping(path = "/users/patient/image")
    public JsonObject getPatientImage(@RequestHeader("token") String token,
                                      @RequestParam String userID) {
        try {
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(UUID.fromString(userID)));
            if (patient.getImage() != null) {
                JsonObject response = new JsonObject();
                response.put("image", patient.getImage());
                return response;
            }
            JsonObject response = new JsonObject();
            response.put("message", "Patient does not have an image");
            return response;
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return exceptionResponse;
        }
    }

    @PostMapping(path = "/upload/games")
    public JsonObject uploadGames(@RequestHeader("token") String token,
                                    @RequestParam MultipartFile file,
                                    @RequestParam String type,
                                    @RequestParam String name,
                                    @RequestParam String description,
                                    @RequestParam boolean useStatistic,
                                    @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        try {
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));

            Game game;
            if (this.gameService.checkGameByName(name)){
                game = this.gameService.getGameByName(name);
                game.setActiveGameStatus(GameActiveStatus.ACTIVE);
            }
            else {
                game = new Game(UUID.randomUUID() ,name, type, description, null, LocalDateTime.now(), useStatistic, GameActiveStatus.ACTIVE);
            }

            String url = "/app/games/" + game.getId().toString();
//                String sd = wrapper+ "/" +game.getId().toString()+"/";
            //Unzip
            File tempFile = File.createTempFile("prefix-", "-suffix");
//            tempFile.deleteOnExit();
            file.transferTo(tempFile);
            ZipFile zipFile = new ZipFile(tempFile);
            zipFile.extractAll(url);
            tempFile.delete();

            game.setUrl(wrapper+ "/" +game.getId().toString()+"/");


            if (image != null) {
                System.out.println(1);
                String orgName = UUID.randomUUID() + "." + FilenameUtils.getExtension(image.getOriginalFilename());
                System.out.println(2);
                if(game.getImage() != null){
                    System.out.println(3);
//                new File("/app/images/" + tutor.getImage().replace(site_url + "images/", "")).delete();
                    new File("/app/images/" + game.getImage()).delete();
                    System.out.println(4);
                    System.out.println("Old image was deleted");
                }
                System.out.println(5);
                File filesd = new File("/app/images", orgName);
                System.out.println(6);
                FileUtils.writeByteArrayToFile(filesd, image.getBytes());
                System.out.println(7);
                game.setImage(orgName);
                System.out.println(8);
//                String orgName = UUID.randomUUID() + "." + FilenameUtils.getExtension(image.getOriginalFilename());
//                if(game.getImage() != null){
//                    System.out.println("SSSSSSSSSSSSSSSSSSS");
//                    System.out.println(String.format("game.getImage(): (%s)", game.getImage()));
////                new File("/app/images/" + tutor.getImage().replace(site_url + "images/", "")).delete();
//                    new File("/app/images/" + game.getImage()).delete();
//                    System.out.println("Old image was deleted");
//
//                }
//                File filesd = new File("/app/images", orgName);
//                FileUtils.writeByteArrayToFile(filesd, file.getBytes());
//                game.setImage(orgName);
            }
            this.gameService.createGame(game);

            JsonObject response = new JsonObject();
            response.put("message", "games successfully uploaded");
            return response;
        }
        catch (Exception e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return exceptionResponse;
        }
    }

    @PostMapping(path = "/upload/games/image")
    public JsonObject uploadTutorImage(@RequestHeader("token") String token,
                                         @RequestParam String gameID,
                                         @RequestParam MultipartFile file){
        try {
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Game game = this.gameService.getGameById(UUID.fromString(gameID));

//            String orgName = game.getName() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            String orgName = UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());

//            Path filepath = Paths.get("/app/images", orgName);
//            if(new  File(filepath.toString()).exists()){
//                System.out.println("File exists");
//                new File(filepath.toString()).delete();
//            }
            if(game.getImage() != null){
                new File("/app/images/" + game.getImage()).delete();
                System.out.println("Old image was deleted");

            }
            File filesd = new File("/app/images", orgName);
            FileUtils.writeByteArrayToFile(filesd, file.getBytes());
//            String url = site_url + "images/" + orgName;
            game.setImage(orgName);

            this.gameService.updateGame(game);
            JsonObject response = new JsonObject();
            response.put("message", "Game image was edited");
            response.put("image", orgName);
            return response;
        }
        catch (Exception e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return exceptionResponse;
        }
    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    @PostMapping(path = "/user/password/edit")
    public JsonObject changePassword(@RequestHeader("token") String token,
                                       @Valid @RequestBody ChangePasswordRequest changePasswordRequest){
        try {
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            User user = this.userService.getUserById(UUID.fromString(changePasswordRequest.getUser_id()));
            String password = changePasswordRequest.getPassword();
            String hashString = BCrypt.withDefaults().hashToString(13, password.toCharArray());
            user.setPassword(hashString);
            this.userService.saveUser(user);
            JsonObject response = new JsonObject();
            response.put("message", "Password was changed");
            return response;
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return exceptionResponse;
        }
    }

    @PostMapping(path = "/user/email/edit")
    public JsonObject changePassword(@RequestHeader("token") String token,
                                       @Valid @RequestBody ChangeEmailRequest changeEmailRequest){
        try {
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            User user = this.userService.getUserById(UUID.fromString(changeEmailRequest.getUser_id()));
            user.setEmail(changeEmailRequest.getEmail());
            this.userService.saveUser(user);
            JsonObject response = new JsonObject();
            response.put("message", "Email was changed");
            return response;
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return exceptionResponse;
        }
    }

    @GetMapping(path = "/organizations/all")
    public JsonObject getOrganizations(@RequestHeader("token") String token,
                                         @RequestParam(value = "limit", required = true) Integer limit,
                                         @RequestParam(value = "start", required = true) Integer start,
                                         @RequestParam(value = "regex", required = false, defaultValue = "") String regex,
                                         @RequestParam(value = "order", required = false, defaultValue = "1") String order) {
        try {
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            JsonObject response = new JsonObject();
            int listCount = this.organizationService.getAllOrganizationsFilteredCount(regex);

            if (listCount == 0) {
                response.put("previous", false);
                response.put("next", false);
                response.put("count", 0);
                response.put("size", 0);
                response.put("results", new ArrayList<>());
                return response;
            }

            if (start >= listCount)
                throw new IllegalArgumentException(String.format("No element at that index (%s)", start));
            List<Organization> organizationList = this.organizationService.getAllOrganizationsFilteredOrderedPaginated(regex, order, start, limit);

            if (start == 0)
                response.put("previous", false);
            else response.put("previous", true);

            if (start + limit > listCount)
                response.put("next", false);
            else response.put("next", true);

            response.put("count", organizationList.size());
            response.put("size", listCount);
            response.put("results", organizationList);
            return response;

//            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
//            List<Organization> organizationList = this.organizationService.getAllOrganizationsFilteredOrdered(regex, order);
//            JsonObject response = new JsonObject();
//
//            if (organizationList.size() == 0) {
////                JsonObject response = new JsonObject();
//                response.put("previous", false);
//                response.put("next", false);
//                response.put("count", 0);
//                response.put("size", 0);
//                response.put("results", new ArrayList<>());
//                return new JSONResponse(200, response);
//            }
//            //Pagination
//            List<Organization> paginatedOrganizationList = new ArrayList<>();
//            if (start >= organizationList.size())
//                throw new IllegalArgumentException(
//                        String.format("No element at that index (%s)", start)
//                );
//            int lastIndex;
//            if (start + limit > organizationList.size()){
//                lastIndex = organizationList.size();
//                response.put("next", false);
//            }
//            else {
//                lastIndex = start + limit;
//                response.put("next", true);
//            }
//            if (start == 0) response.put("previous", false);
//            else response.put("previous", true);
//
//            for (int i = start; i < lastIndex; i++){
//                paginatedOrganizationList.add(organizationList.get(i));
//            }
//            response.put("count", paginatedOrganizationList.size());
//            response.put("size", organizationList.size());
//
////            response.put("userList", this.userService.getAllUsers());
//            response.put("results", paginatedOrganizationList);
//            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return exceptionResponse;
        }
    }

    @PostMapping("/organizations/add")
    public JsonObject createOrganization(@RequestHeader("token") String token,
                                           @Valid @RequestBody OrganizationForAdding organizationForAdding) {
        try {
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Organization organization = new Organization(organizationForAdding.getName(), organizationForAdding.getAddress(), organizationForAdding.getPhone(),
                    organizationForAdding.getEmail(), organizationForAdding.getWebsite(), LocalDateTime.now());
            this.organizationService.createOrganization(organization);
            JsonObject response = new JsonObject();
            response.put("message", "Organization was created");
            return response;
        } catch (IllegalArgumentException e) {
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return exceptionResponse;
        }
    }

    @DeleteMapping("/organizations/delete")
    public JsonObject deleteOrganization(@RequestHeader("token") String token,
                                           @Valid @RequestBody OrganizationToDelete organizationToDelete) {
        try {
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Organization organization = this.organizationService.getOrganizationById(UUID.fromString(organizationToDelete.getOrganization()));

            //TODO улучшить
            List<Researcher> researchers = this.researcherService.getAllResearchersByOrganization(organization);
            for (Researcher researcher: researchers){
                researcher.setOrganization(null);
                this.researcherService.updateResearcher(researcher);
            }
            List<Tutor> tutors = this.tutorService.getAllTutorsByOrganization(organization);
            for (Tutor tutor: tutors) {
                tutor.setOrganization(null);
                this.tutorService.updateTutor(tutor);
            }
            List<Patient> patients = this.patientService.getAllPatientsByOrganization(organization);
            for (Patient patient: patients) {
                patient.setOrganization(null);
                this.patientService.updatePatient(patient);
            }

            this.organizationService.deleteOrganization(organization);
            JsonObject response = new JsonObject();
            response.put("message", "Organization was deleted");
            return response;

        } catch (IllegalArgumentException e) {
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return exceptionResponse;
        }
    }

    @PostMapping("/assign/role")
    public JsonObject assignRoleUser(@RequestHeader("token") String token,
                                       @Valid @RequestBody RoleForAssign roleForAssign) {
        try {
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            User userForAssign = this.userService.getUserById(UUID.fromString(roleForAssign.getUser()));
            //TODO: Delete organization
            Organization testOrganization = this.organizationService.getOrganizationByName("TestOrganization");
            Integer role = roleForAssign.getRole();
            switch (role) {
                case 0 -> {
                    userForAssign.setRole(role);
                    this.userService.updateUser(userForAssign);
                    Admin user_admin = new Admin(userForAssign);
                    this.adminService.createAdmin(user_admin);
                    JsonObject response = new JsonObject();
                    response.put("message", "Admin was assigned");
                    return response;
                }
                case 1 -> {
                    userForAssign.setRole(role);
                    this.userService.updateUser(userForAssign);
                    Patient patient = new Patient(userForAssign);
                    //TODO: Delete Organization later.
                    patient.setOrganization(testOrganization);
                    this.patientService.createPatient(patient);
                    JsonObject response = new JsonObject();
                    response.put("message", "Patient was assigned");
                    return response;
                }
                case 2 -> {
                    userForAssign.setRole(role);
                    this.userService.updateUser(userForAssign);
                    Researcher researcher = new Researcher(userForAssign);
                    //TODO: Delete Organization later.
                    researcher.setOrganization(testOrganization);
                    this.researcherService.createResearcher(researcher);
                    JsonObject response = new JsonObject();
                    response.put("message", "Researcher was assigned");
                    return response;
                }
                case 3 -> {
                    userForAssign.setRole(role);
                    this.userService.updateUser(userForAssign);
                    Tutor tutor = new Tutor(userForAssign);
                    //TODO: Delete Organization later.
                    tutor.setOrganization(testOrganization);
                    this.tutorService.createTutor(tutor);
                    JsonObject response = new JsonObject();
                    response.put("message", "Tutor was assigned");
                    return response;
                }
            }
        } catch (IllegalArgumentException e) {
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return exceptionResponse;
        }
        JsonObject defaultResponse = new JsonObject();
        defaultResponse.put("message", "No such role");
        return defaultResponse;
    }

    @PostMapping("/assign/organization")
    public JsonObject assignOrganization(@RequestHeader("token") String token,
                                           @Valid @RequestBody OrganizationForAssign organizationForAssign) {
        try {
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            User userForAssign = this.userService.getUserById(UUID.fromString(organizationForAssign.getUser()));
            Organization newOrganization = this.organizationService.getOrganizationById(UUID.fromString(organizationForAssign.getOrganization()));

            switch (userForAssign.getRole()){
                case 1 -> {
                    Patient patient = patientService.getPatientByUser(userForAssign);
                    patient.setOrganization(newOrganization);
                    this.patientService.updatePatient(patient);
                }
                case 2 -> {
                    Researcher researcher = researcherService.getResearcherByUser(userForAssign);
                    researcher.setOrganization(newOrganization);
                    this.researcherService.updateResearcher(researcher);
                }
                case 3 -> {
                    Tutor tutor = tutorService.getTutorByUser(userForAssign);
                    tutor.setOrganization(newOrganization);
                    this.tutorService.updateTutor(tutor);
                }
                default -> {
                    JsonObject exceptionResponse = new JsonObject();
                    exceptionResponse.put("message", String.format("User %s was not founded in roles", userForAssign.getId()));
                    return exceptionResponse;
                }
            }
            JsonObject response = new JsonObject();
            response.put("message", "Organization was assigned");
            return response;
        } catch (IllegalArgumentException e) {
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return exceptionResponse;
        }

    }

    @PutMapping(path = "/users/patient/editing")
    public JsonObject editAccountOfPatient(@RequestHeader("token") String token,
                                             @Valid @RequestBody EditPatientInfoByAdminRequest editPatientInfoByAdminRequest) {
        try {
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Patient patient = this.patientService.getPatientByUser(this.userService.getUserById(UUID.fromString(editPatientInfoByAdminRequest.getId())));
            if (editPatientInfoByAdminRequest.getName() != null){
                patient.setName(editPatientInfoByAdminRequest.getName());
            }
            if (editPatientInfoByAdminRequest.getSurname() != null){
                patient.setSurname(editPatientInfoByAdminRequest.getSurname());
            }
            if (editPatientInfoByAdminRequest.getSecondName() != null){
                patient.setSecondName(editPatientInfoByAdminRequest.getSecondName());
            }
            if (editPatientInfoByAdminRequest.getGender() != null){
                patient.setGender(Gender.valueOf(editPatientInfoByAdminRequest.getGender()));
            }
            if (editPatientInfoByAdminRequest.getOrganization() != null){
                patient.setOrganization(this.organizationService.getOrganizationById(UUID.fromString(editPatientInfoByAdminRequest.getOrganization())));
            }
            if (editPatientInfoByAdminRequest.getBirthDate() != null){
                patient.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse(editPatientInfoByAdminRequest.getBirthDate()));
            }
            if (editPatientInfoByAdminRequest.getNumberPhone() != null){
                patient.setNumberPhone(editPatientInfoByAdminRequest.getNumberPhone());
            }
            if (editPatientInfoByAdminRequest.getAddress() != null){
                patient.setAddress(editPatientInfoByAdminRequest.getAddress());
            }
            this.patientService.updatePatient(patient);
            JsonObject response = new JsonObject();
            response.put("message", "Patient account was edited");
            return response;
        }
        catch (Exception e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return exceptionResponse;
        }
    }

    @PutMapping(path = "/users/tutor/editing")
    public JsonObject editAccountOfTutor(@RequestHeader("token") String token,
                                           @Valid @RequestBody EditTutorInfoByAdminRequest editTutorInfoByAdminRequest) {
        try {
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Tutor tutor = this.tutorService.getTutorByUser(this.userService.getUserById(UUID.fromString(editTutorInfoByAdminRequest.getId())));
            if (editTutorInfoByAdminRequest.getName() != null) {
                tutor.setName(editTutorInfoByAdminRequest.getName());
            }
            if (editTutorInfoByAdminRequest.getSurname() != null) {
                tutor.setSurname(editTutorInfoByAdminRequest.getSurname());
            }
            if (editTutorInfoByAdminRequest.getSecondName() != null) {
                tutor.setSecondName(editTutorInfoByAdminRequest.getSecondName());
            }
            if (editTutorInfoByAdminRequest.getOrganization() != null) {
                tutor.setOrganization(this.organizationService.getOrganizationById(UUID.fromString(editTutorInfoByAdminRequest.getOrganization())));
            }
            this.tutorService.updateTutor(tutor);
            JsonObject response = new JsonObject();
            response.put("message", "Tutor account was edited");
            return response;
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return exceptionResponse;
        }
    }
}
