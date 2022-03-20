package com.yoh.backend.service;

import com.yoh.backend.entity.Answer;
import com.yoh.backend.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    public void createAnswer(Answer answer) throws IllegalArgumentException{
        // TODO Добоавить валидацию и проверку на существование

        checkExistAnswer(answer);
        answerRepository.createAnswer(answer);
    }

    private void checkExistAnswer(Answer answer) throws IllegalArgumentException{

    }

    public Answer getAnswerById(UUID id) throws IllegalArgumentException{
        Answer answer = answerRepository.getAnswerByUUID(id);
        if (answer != null) {
            return answer;
        }
        else throw new IllegalArgumentException(
                String.format("Sorry, but Answer with this id (%s) wasn't found.", id)
        );
    }
}