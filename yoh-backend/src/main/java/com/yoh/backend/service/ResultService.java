package com.yoh.backend.service;

import com.yoh.backend.entity.Result;
import com.yoh.backend.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ResultService {

    @Autowired
    private ResultRepository resultRepository;

    public void createResult(Result result) throws IllegalArgumentException{
        // TODO Добоавить валидацию и проверку на существование

        checkExistResult(result);
        resultRepository.createResult(result);
    }

    private void checkExistResult(Result result) throws IllegalArgumentException{

    }

    public Result getResultById(UUID id) throws IllegalArgumentException{
        Result result = resultRepository.getResultByUUID(id);
        if (result != null) {
            return result;
        }
        else throw new IllegalArgumentException(
                String.format("Sorry, but Result with this id (%s) wasn't found.", id)
        );
    }
}