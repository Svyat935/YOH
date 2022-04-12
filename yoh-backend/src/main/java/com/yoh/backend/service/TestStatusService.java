package com.yoh.backend.service;

import com.yoh.backend.entity.*;
import com.yoh.backend.repository.TestStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TestStatusService {

    @Autowired
    private TestStatusRepository testStatusRepository;

    public void createTestStatus(TestStatus testStatus) throws IllegalArgumentException{
        // TODO Добоавить валидацию и проверку на существование

        checkExistTestStatus(testStatus);
        testStatusRepository.createTestStatus(testStatus);
    }

    public void updateTestStatus(TestStatus testStatus) throws IllegalArgumentException{
        testStatusRepository.createTestStatus(testStatus);
    }

    public void deleteTestStatus(TestStatus testStatus) throws IllegalArgumentException{
        testStatusRepository.deleteTestStatus(testStatus);
    }

    private void checkExistTestStatus(TestStatus testStatus) throws IllegalArgumentException{

    }

    public List<TestStatus> getAllTestStatuses(){
        return testStatusRepository.getAllTestStatuses();
    }

    public TestStatus getTestStatusById(UUID id) throws IllegalArgumentException{
        TestStatus testStatus = testStatusRepository.getTestStatusByUUID(id);
        if (testStatus != null) {
            return testStatus;
        }
        else throw new IllegalArgumentException(
                String.format("Sorry, but TestStatus with this id (%s) wasn't found.", id)
        );
    }

    public TestStatus getTestStatusByTestAndPatient(Test test, Patient patient) throws IllegalArgumentException{
        TestStatus testStatus = testStatusRepository.getTestStatusByTestAndPatient(test, patient);
        if (testStatus != null) {
            return testStatus;
        }
        else throw new IllegalArgumentException(
                String.format("Sorry, but TestStatus with this test (%s) and patient (%s) wasn't found.", test, patient)
        );
    }
}
