package com.yoh.backend.service;

import com.yoh.backend.entity.Test;
import com.yoh.backend.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    public void createTest(Test test) throws IllegalArgumentException{
        // TODO Добоавить валидацию и проверку на существование

        checkExistTest(test);
        testRepository.createTest(test);
    }

    private void checkExistTest(Test test) throws IllegalArgumentException{

    }

    public Test getTestById(UUID id) throws IllegalArgumentException{
        Test test = testRepository.getTestByUUID(id);
        if (test != null) {
            return test;
        }
        else throw new IllegalArgumentException(
                String.format("Sorry, but Test with this id (%s) wasn't found.", id)
        );
    }
}
