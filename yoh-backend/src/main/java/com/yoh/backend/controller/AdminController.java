package com.yoh.backend.controller;

import antlr.StringUtils;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.yoh.backend.entity.*;
import com.yoh.backend.request.*;
import com.yoh.backend.response.JSONResponse;
import com.yoh.backend.response.UserInfoResponse;
import com.yoh.backend.service.*;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.UnzipParameters;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.*;
import net.lingala.zip4j.*;


@RestController
@RequestMapping("/admins")
public class AdminController {
    @Value("${GAMES_FOLDER}")
    private String games_folder;

    @Value("${WRAPPER}")
    private String wrapper;

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
    public JSONResponse getUsers(@RequestHeader("token") String token) {
        try {
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            JsonObject response = new JsonObject();
            List<UserInfoResponse> responseList = new ArrayList<>();
            for (User user: this.userService.getAllUsers()) {
                responseList.add(new UserInfoResponse(user));
            }
//            response.put("userList", this.userService.getAllUsers());
            response.put("userList", responseList);
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @PostMapping(path = "/upload/games")
    public JSONResponse uploadGames(@RequestHeader("token") String token, @RequestParam MultipartFile file, @RequestParam String name, @RequestParam String description) {
        try {
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            if(this.gameService.checkGameByName(name)){
                String url = "/app/games/" + name;

                //Unzip
                File tempFile = File.createTempFile("prefix-", "-suffix");
//            tempFile.deleteOnExit();
                file.transferTo(tempFile);
                ZipFile zipFile = new ZipFile(tempFile);
                zipFile.extractAll(url);
                tempFile.delete();

                Game game = new Game(name, description, wrapper+ "/" +name+"/", LocalDateTime.now());
                this.gameService.createGame(game);

                JsonObject response = new JsonObject();
                response.put("message", "games successfully uploaded");
                return new JSONResponse(200, response);
            }
            else {
                JsonObject response = new JsonObject();
                response.put("message", "Game is already exists");
                return new JSONResponse(401, response);
            }

//            String destDir = "/app/games";
//            ----------------------------------------------------
//            File destDir = new File("/app/games");
//
//            if(!destDir.exists()) destDir.mkdirs();
//
//            try {
//                byte[] buffer = new byte[1024];
//                ZipInputStream zis = new ZipInputStream(file.getInputStream());
//                ZipEntry zipEntry = zis.getNextEntry();
//                while (zipEntry != null) {
//                    System.out.println(zipEntry);
//                    File newFile = newFile(destDir, zipEntry);
//                    if (zipEntry.isDirectory()) {
//                        if (!newFile.isDirectory() && !newFile.mkdirs()) {
//                            throw new IOException("Failed to create directory " + newFile);
//                        }
//                    } else {
//                        // fix for Windows-created archives
//                        File parent = newFile.getParentFile();
//                        if (!parent.isDirectory() && !parent.mkdirs()) {
//                            throw new IOException("Failed to create directory " + parent);
//                        }
//
//                        // write file content
//                        FileOutputStream fos = new FileOutputStream(newFile);
//                        int len;
//                        while ((len = zis.read(buffer)) > 0) {
//                            fos.write(buffer, 0, len);
//                        }
//                        fos.close();
//                    }
//                    zipEntry = zis.getNextEntry();
//                }
//                zis.closeEntry();
//                zis.close();
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//            _________________________________________________________

//            byte[] bytes = file.getBytes();
//            File tempFile = File.createTempFile("prefix-", "-suffix");
////            tempFile.deleteOnExit();
//            file.transferTo(tempFile);
//            ZipFile zipFile = new ZipFile(tempFile);
//            System.out.println("+++++++++++++++++++");
//            System.out.println(zipFile.getFileHeaders());
//            System.out.println("+++++++++++++++++++");
//            zipFile.extractFile("games_archive/2/", "/app/games/");
////            zipFile.extractAll("/app/games");
//
////            ZipFile zipFile = new ZipFile(tempFile);
////            zipFile
//            tempFile.delete();






//            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
//            byte[] bytes = file.getBytes();
//            byte[] buffer = new byte[4096];
////            ZipInputStream zis = new ZipInputStream(file.getInputStream());
//
//            ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(bytes));
//            System.out.println("Processing archive with size=" + file.getSize());
//            ZipEntry entry = zis.getNextEntry();
//            System.out.println(entry);
//
//            while (entry != null) {
//                System.out.println("Processing file = " + entry.getName() + " is directory? " + entry.isDirectory());
//                File newFile = new File("games/" + entry.getName());
//                System.out.println("Unzipping to "+newFile.getAbsolutePath());
////                new File(newFile.getParent()).mkdirs();
////                FileOutputStream fos = new FileOutputStream(newFile);
////                int len;
////                while ((len = zis.read(buffer)) > 0) {
////                    fos.write(buffer, 0, len);
////                }
////                fos.close();
//                zis.closeEntry();
//                entry = zis.getNextEntry();
//            }
//            zis.closeEntry();
//            zis.close();


        }
        catch (Exception e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
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

    @GetMapping(path = "/organizations/all")
    public JSONResponse getOrganizations(@RequestHeader("token") String token) {
        try {
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            JsonObject response = new JsonObject();
            response.put("organizationList", this.organizationService.getAllOrganizations());
            return new JSONResponse(200, response);
        }
        catch (IllegalArgumentException e){
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @PostMapping("/organizations/add")
    public JSONResponse createOrganization(@RequestHeader("token") String token, @Valid @RequestBody OrganizationForAdding organizationForAdding) {
        try {
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            Organization organization = new Organization(organizationForAdding.getName(), organizationForAdding.getAddress(), organizationForAdding.getPhone(),
                    organizationForAdding.getEmail(), organizationForAdding.getWebsite());
            this.organizationService.createOrganization(organization);
            JsonObject response = new JsonObject();
            response.put("message", "Organization was created");
            return new JSONResponse(200, response);
        } catch (IllegalArgumentException e) {
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @DeleteMapping("/organizations/delete")
    public JSONResponse deleteOrganization(@RequestHeader("token") String token, @Valid @RequestBody OrganizationToDelete organizationToDelete) {
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
            return new JSONResponse(200, response);

        } catch (IllegalArgumentException e) {
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
    }

    @PostMapping("/assign/role")
    public JSONResponse assignRoleUser(@RequestHeader("token") String token, @Valid @RequestBody RoleForAssign roleForAssign) {
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
                    return new JSONResponse(200, response);
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
                    return new JSONResponse(200, response);
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
                    return new JSONResponse(200, response);
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
                    return new JSONResponse(200, response);
                }
            }
        } catch (IllegalArgumentException e) {
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }
        JsonObject defaultResponse = new JsonObject();
        defaultResponse.put("message", "No such role");
        return new JSONResponse(401, defaultResponse);
    }

    @PostMapping("/assign/organization")
    public JSONResponse assignOrganization(@RequestHeader("token") String token, @Valid @RequestBody OrganizationForAssign organizationForAssign) {
        try {
            Admin admin = this.adminService.getAdminByUser(this.userService.getUserById(this.userService.verifyToken(token)));
            User userForAssign = this.userService.getUserById(UUID.fromString(organizationForAssign.getUser()));
            Organization newOrganization = this.organizationService.getOrganizationById(UUID.fromString(organizationForAssign.getOrganization()));

            Patient patient = patientService.getPatientByUser(userForAssign);
            if (patient == null) {
                Researcher researcher = researcherService.getResearcherByUser(userForAssign);
                if (researcher == null) {
                    Tutor tutor = tutorService.getTutorByUser(userForAssign);
                    if (tutor != null) {
                        tutor.setOrganization(newOrganization);
                        this.tutorService.updateTutor(tutor);
                    } else {
                        JsonObject exceptionResponse = new JsonObject();
                        exceptionResponse.put("message", String.format("User was not founded in roles %s", userForAssign.getId()));
                        return new JSONResponse(401, exceptionResponse);
                    }
                } else {
                    researcher.setOrganization(newOrganization);
                    this.researcherService.updateResearcher(researcher);
                }
            } else {
                patient.setOrganization(newOrganization);
                this.patientService.updatePatient(patient);
            }
            JsonObject response = new JsonObject();
            response.put("message", "Organization was assigned");
            return new JSONResponse(200, response);
        } catch (IllegalArgumentException e) {
            JsonObject exceptionResponse = new JsonObject();
            exceptionResponse.put("message", e.getMessage());
            return new JSONResponse(401, exceptionResponse);
        }

    }
}
