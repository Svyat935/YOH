package com.yoh.backend.service;

import com.yoh.backend.entity.Game;
import com.yoh.backend.entity.GameStatistic;
import com.yoh.backend.entity.GameStatus;
import com.yoh.backend.entity.Patient;
import com.yoh.backend.repository.GameStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GameStatusService {

    @Autowired
    private GameStatusRepository gameStatusRepository;

    public void createGameStatus(GameStatus gameStatus) throws IllegalArgumentException{
        // TODO Добоавить валидацию и проверку на существование

        checkExistGameStatus(gameStatus);
        gameStatusRepository.createGameStatus(gameStatus);
    }

    public void updateGameStatus(GameStatus gameStatus) throws IllegalArgumentException{
        gameStatusRepository.createGameStatus(gameStatus);
    }

    public void deleteGameStatus(GameStatus gameStatus) throws IllegalArgumentException{
        gameStatusRepository.deleteGameStatus(gameStatus);
    }

    private void checkExistGameStatus(GameStatus gameStatus) throws IllegalArgumentException{

    }

    public List<GameStatus> getAllGameStatuses(){
        return gameStatusRepository.getAllGameStatuses();
    }

    public GameStatus getGameStatusById(UUID id) throws IllegalArgumentException{
        GameStatus gameStatus = gameStatusRepository.getGameStatusByUUID(id);
        if (gameStatus != null) {
            return gameStatus;
        }
        else throw new IllegalArgumentException(
                String.format("Sorry, but GameStatus with this id (%s) wasn't found.", id)
        );
    }

    public GameStatus getGameStatusByGameAndPatient(Game game, Patient patient) throws IllegalArgumentException{
        GameStatus gameStatus = gameStatusRepository.getGameStatusByGameAndPatient(game, patient);
        if (gameStatus != null) {
            return gameStatus;
        }
        else throw new IllegalArgumentException(
                String.format("Sorry, but GameStatus with this game (%s) and patient (%s) wasn't found.", game, patient)
        );
    }
}