package com.yoh.backend.service;

import com.yoh.backend.entity.GameStatistic;
import com.yoh.backend.repository.GameStatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GameStatisticService {

    @Autowired
    private GameStatisticRepository gameStatisticRepository;

    public void createGameStatistic(GameStatistic gameStatistic) throws IllegalArgumentException{
        // TODO Добоавить валидацию и проверку на существование

        checkExistGameStatistic(gameStatistic);
        gameStatisticRepository.createGameStatistic(gameStatistic);
    }

    public void updateGameStatistic(GameStatistic gameStatistic) throws IllegalArgumentException{
        gameStatisticRepository.createGameStatistic(gameStatistic);
    }


    private void checkExistGameStatistic(GameStatistic gameStatistic) throws IllegalArgumentException{

    }

    public GameStatistic getGameStatisticById(UUID id) throws IllegalArgumentException{
        GameStatistic gameStatistic = gameStatisticRepository.getGameStatisticByUUID(id);
        if (gameStatistic != null) {
            return gameStatistic;
        }
        else throw new IllegalArgumentException(
                String.format("Sorry, but GameStatistic with this id (%s) wasn't found.", id)
        );
    }
}
