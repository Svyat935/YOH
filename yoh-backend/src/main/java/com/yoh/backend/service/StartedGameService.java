package com.yoh.backend.service;

import com.yoh.backend.entity.GamePatient;
import com.yoh.backend.entity.GameStatistic;
import com.yoh.backend.entity.StartedGame;
import com.yoh.backend.repository.StartedGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StartedGameService {
    @Autowired
    private StartedGameRepository startedGameRepository;

    public void saveStartedGame(StartedGame startedGame) throws IllegalArgumentException{
        startedGameRepository.saveStartedGame(startedGame);
    }

    public String getLatestDetailsByGamePatient(GamePatient gamePatient) {
        return startedGameRepository.getLatestDetailsByGamePatient(gamePatient);
    }

    public StartedGame getLatestStartedGameByGamePatient(GamePatient gamePatient) {
        return startedGameRepository.getLatestStartedGameByGamePatient(gamePatient);
    }

    public StartedGame getUnfinishedStartedGameByGamePatient(GamePatient gamePatient) {
        return startedGameRepository.getUnfinishedStartedGameByGamePatient(gamePatient);
    }
}
