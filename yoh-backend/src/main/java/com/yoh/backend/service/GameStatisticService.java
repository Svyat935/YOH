package com.yoh.backend.service;

import com.yoh.backend.entity.GamePatient;
import com.yoh.backend.entity.GameStatistic;
import com.yoh.backend.entity.StartedGame;
import com.yoh.backend.repository.GameStatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameStatisticService {

    @Autowired
    private GameStatisticRepository gameStatisticRepository;

    public void saveGameStatistic(GameStatistic gameStatistic) throws IllegalArgumentException{
        gameStatisticRepository.saveGameStatistic(gameStatistic);
    }

    public GameStatistic getGameStatisticByStartedGame(StartedGame startedGame) {
        return gameStatisticRepository.getGameStatisticByStartedGame(startedGame);
    }

    public List<?> sdasdasds(GamePatient gamePatient) throws IllegalArgumentException{
        return gameStatisticRepository.sdsasdew(gamePatient);
    }

//    public void createGameStatistic(GameStatistic gameStatistic) throws IllegalArgumentException{
//        // TODO Добоавить валидацию и проверку на существование
//
//        checkExistGameStatistic(gameStatistic);
//        gameStatisticRepository.createGameStatistic(gameStatistic);
//    }
//
//    public void updateGameStatistic(GameStatistic gameStatistic) throws IllegalArgumentException{
//        gameStatisticRepository.createGameStatistic(gameStatistic);
//    }
//
//    public void deleteGameStatistic(GameStatistic gameStatistic) throws IllegalArgumentException{
//        gameStatisticRepository.deleteGameStatistic(gameStatistic);
//    }
//
//
//    private void checkExistGameStatistic(GameStatistic gameStatistic) throws IllegalArgumentException{
//
//    }
//
//    public List<GameStatistic> getAllGameStatistics(){
//        return gameStatisticRepository.getAllGameStatistics();
//    }
//
//    public GameStatistic getGameStatisticById(UUID id) throws IllegalArgumentException{
//        GameStatistic gameStatistic = gameStatisticRepository.getGameStatisticByUUID(id);
//        if (gameStatistic != null) {
//            return gameStatistic;
//        }
//        else throw new IllegalArgumentException(
//                String.format("Sorry, but GameStatistic with this id (%s) wasn't found.", id)
//        );
//    }
//
//    public List<GameStatistic> getGameStatisticByGamePatient(GamePatient gamePatient) throws IllegalArgumentException{
//        return gameStatisticRepository.getGameStatisticByGamePatient(gamePatient);
//    }
}
