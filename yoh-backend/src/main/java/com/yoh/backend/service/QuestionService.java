package com.yoh.backend.service;

import com.yoh.backend.entity.Game;
import com.yoh.backend.entity.Question;
import com.yoh.backend.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public void createQuestion(Question question) throws IllegalArgumentException{
        // TODO Добоавить валидацию и проверку на существование

        checkExistQuestion(question);
        questionRepository.createQuestion(question);
    }

    public void updateQuestion(Question question) throws IllegalArgumentException{
        questionRepository.createQuestion(question);
    }

    public void deleteQuestion(Question question) throws IllegalArgumentException{
        questionRepository.deleteQuestion(question);
    }

    private void checkExistQuestion(Question question) throws IllegalArgumentException{

    }

    public List<Question> getAllQuestions(){
        return questionRepository.getAllQuestions();
    }

    public Question getQuestionById(UUID id) throws IllegalArgumentException{
        Question question = questionRepository.getQuestionByUUID(id);
        if (question != null) {
            return question;
        }
        else throw new IllegalArgumentException(
                String.format("Sorry, but Question with this id (%s) wasn't found.", id)
        );
    }
}