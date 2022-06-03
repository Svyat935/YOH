package com.yoh.backend.service;

import com.yoh.backend.entity.*;
import com.yoh.backend.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public void createPatient(Patient patient) throws IllegalArgumentException{
        // TODO Добоавить валидацию и проверку на существование

        patientRepository.createPatient(patient);
    }

    public void updatePatient(Patient patient) throws IllegalArgumentException{
        patientRepository.createPatient(patient);
    }

    public void deletePatient(Patient patient) throws IllegalArgumentException{
        patientRepository.deletePatient(patient);
    }

    public List<Patient> getAllPatients(){
        return patientRepository.getAllPatients();
    }

    public Patient getPatientByUser(User user) {
        return patientRepository.getPatientByUser(user);
    }

    public Patient getPatientById(UUID id) throws IllegalArgumentException{
        Patient patient = patientRepository.getPatientByUUID(id);
        if (patient != null) {
            return patient;
        }
        else throw new IllegalArgumentException(
                String.format("Sorry, but Patient with this id (%s) wasn't found.", id)
        );
    }

    public List<Patient> getAllPatientsByOrganizationFiltered(Organization organization, String regex, String order) {
        return patientRepository.getAllPatientsByOrganization(organization, order, regex);
    }

    public int getAllPatientsByOrganizationFilteredCount(Organization organization, String regex) {
        return patientRepository.getAllPatientsByOrganizationFilteredCount(organization, regex);
    }

    public List<Patient> getAllPatientsByOrganizationFilteredPaginated(Organization organization, String regex, String order, int start, int limit) {
        return patientRepository.getAllPatientsByOrganizationFilteredPaginated(organization, order, regex, start, limit);
    }

    public List<Patient> getAllPatientsByOrganization(Organization organization){
        return patientRepository.getAllPatientsByOrganization(organization, "", "");
    }

}
