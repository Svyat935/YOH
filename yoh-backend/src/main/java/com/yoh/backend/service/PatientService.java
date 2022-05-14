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

    public List<Patient> getAllPatientsByOrganizationFiltered(Organization organization, String regex, Integer limit, Integer start){
        List<Patient> patientListUnfiltered = patientRepository.getAllPatientsByOrganization(organization);
        if (!regex.equals("")){
            patientListUnfiltered = patientListUnfiltered
                    .stream()
                    .filter(i -> (i.getSurname() != null && i.getSurname().toLowerCase().contains(regex.toLowerCase()))
                            || (i.getName() != null && i.getName().toLowerCase().contains(regex.toLowerCase()))
                            || (i.getSecondName() != null && i.getSecondName().toLowerCase().contains(regex.toLowerCase())))
                    .collect(Collectors.toList());
        }
//        else return patientListUnfiltered;
        if (start >= patientListUnfiltered.size())
            throw new IllegalArgumentException(
                    String.format("No element at that index (%s)", start)
            );
        int lastIndex = Math.min(start + limit, patientListUnfiltered.size());
        List<Patient> result = new ArrayList<>();
        for (int i = start; i < lastIndex; i++){
            result.add(patientListUnfiltered.get(i));
        }
        return result;
//        Patient[] patientsArray = patientListUnfiltered.toArray(new Patient[0]);
//        List<Patient> sdsd = Stream.iterate(0, )
//        return java.util.Arrays.stream(patientsArray, start, lastIndex)
//                .collect(Collectors.toList());
    }

    public List<Patient> getAllPatientsByOrganization(Organization organization){
        return patientRepository.getAllPatientsByOrganization(organization);
    }

}
