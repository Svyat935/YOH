package com.yoh.backend.service;

import com.yoh.backend.entity.Game;
import com.yoh.backend.entity.Researcher;
import com.yoh.backend.entity.User;
import com.yoh.backend.repository.ResearcherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ResearcherService {

    @Autowired
    private ResearcherRepository researcherRepository;

    public void createResearcher(Researcher researcher) throws IllegalArgumentException{
        // TODO Добоавить валидацию и проверку на существование

        researcherRepository.createResearcher(researcher);
    }

    public void updateResearcher(Researcher researcher) throws IllegalArgumentException{
        researcherRepository.createResearcher(researcher);
    }

    public void deleteResearcher(Researcher researcher) throws IllegalArgumentException{
        researcherRepository.deleteResearcher(researcher);
    }

    public List<Researcher> getAllResearchers(){
        return researcherRepository.getAllResearchers();
    }

    public Researcher getResearcherByUser(User user) {
        return researcherRepository.getResearcherByUser(user);
    }

    public Researcher getResearcherById(UUID id) throws IllegalArgumentException{
        Researcher researcher = researcherRepository.getResearcherByUUID(id);
        if (researcher != null) {
            return researcher;
        }
        else throw new IllegalArgumentException(
                String.format("Sorry, but Researcher with this id (%s) wasn't found.", id)
        );
    }
}
