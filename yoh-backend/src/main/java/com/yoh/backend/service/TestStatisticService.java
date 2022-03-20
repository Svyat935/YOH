package com.yoh.backend.service;

import com.yoh.backend.entity.TestStatistic;
import com.yoh.backend.repository.TestStatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TestStatisticService {

    @Autowired
    private TestStatisticRepository testStatisticRepository;

    public void createTestStatistic(TestStatistic testStatistic) throws IllegalArgumentException{
        // TODO Добоавить валидацию и проверку на существование

        checkExistTestStatistic(testStatistic);
        testStatisticRepository.createTestStatistic(testStatistic);
    }

    private void checkExistTestStatistic(TestStatistic testStatistic) throws IllegalArgumentException{

    }

    public TestStatistic getTestStatisticById(UUID id) throws IllegalArgumentException{
        TestStatistic testStatistic = testStatisticRepository.getTestStatisticByUUID(id);
        if (testStatistic != null) {
            return testStatistic;
        }
        else throw new IllegalArgumentException(
                String.format("Sorry, but TestStatistic with this id (%s) wasn't found.", id)
        );
    }
}
