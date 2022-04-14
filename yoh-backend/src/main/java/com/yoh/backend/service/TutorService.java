package com.yoh.backend.service;

import com.yoh.backend.entity.Game;
import com.yoh.backend.entity.Tutor;
import com.yoh.backend.entity.User;
import com.yoh.backend.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TutorService {

    @Autowired
    private TutorRepository tutorRepository;

    public void createTutor(Tutor tutor) throws IllegalArgumentException{
        // TODO Добоавить валидацию и проверку на существование

        tutorRepository.createTutor(tutor);
    }

    public void updateTutor(Tutor tutor) throws IllegalArgumentException{
        tutorRepository.createTutor(tutor);
    }

    public void deleteTutor(Tutor tutor) throws IllegalArgumentException{
        tutorRepository.deleteTutor(tutor);
    }

    public List<Tutor> getAllTutors(){
        return tutorRepository.getAllTutors();
    }

    public Tutor getTutorByUser(User user) throws IllegalArgumentException{
        Tutor tutor = tutorRepository.getTutorByUser(user);
        if (tutor != null) return tutor;
        else throw new IllegalArgumentException(
                String.format("Sorry, but Tutor with this user (%s) wasn't found.", user.getId().toString())
        );
    }

    public Tutor getTutorById(UUID id) throws IllegalArgumentException{
        Tutor tutor = tutorRepository.getTutorByUUID(id);
        if (tutor != null) {
            return tutor;
        }
        else throw new IllegalArgumentException(
                String.format("Sorry, but Tutor with this id (%s) wasn't found.", id)
        );
    }
}
