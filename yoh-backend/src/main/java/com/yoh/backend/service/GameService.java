package com.yoh.backend.service;

import com.yoh.backend.entity.Game;
import com.yoh.backend.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    public void createGame(Game game) throws IllegalArgumentException{
        // TODO Добоавить валидацию и проверку на существование
        checkExistGame(game);
        gameRepository.createGame(game);
    }

    public void updateGame(Game game) throws IllegalArgumentException{
        gameRepository.createGame(game);
    }

    public void deleteGame(Game game) throws IllegalArgumentException{
        gameRepository.deleteGame(game);
    }

    private void checkExistGame(Game game) throws IllegalArgumentException{

    }

    public boolean checkGameByName(String name) throws IllegalArgumentException{
        return gameRepository.getGameByName(name) == null;
    }

    public Game getGameById(UUID id) throws IllegalArgumentException{
        Game game = gameRepository.getGameByUUID(id);
        if (game != null) {
            return game;
        }
        else throw new IllegalArgumentException(
                String.format("Sorry, but Game with this id (%s) wasn't found.", id)
        );
    }

    public List<Game> getAllGames(){
        return gameRepository.getAllGames();
    }
}
