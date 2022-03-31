package com.yoh.backend.service;

import com.yoh.backend.entity.*;
import com.yoh.backend.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


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

    public List<Patient> getAllPatientsByOrganization(Organization organization){
        return patientRepository.getAllPatientsByOrganization(organization);
    }

}
