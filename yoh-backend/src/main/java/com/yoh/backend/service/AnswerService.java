package com.yoh.backend.service;

import com.yoh.backend.entity.Answer;
import com.yoh.backend.entity.Game;
import com.yoh.backend.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public void updateAnswer(Answer answer) throws IllegalArgumentException{
        answerRepository.createAnswer(answer);
    }

    public void deleteAnswer(Answer answer) throws IllegalArgumentException{
        answerRepository.deleteAnswer(answer);
    }

    private void checkExistAnswer(Answer answer) throws IllegalArgumentException{

    }

    public List<Answer> getAllAnswers(){
        return answerRepository.getAllAnswers();
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