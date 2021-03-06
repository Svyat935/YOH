package com.yoh.backend.service;

import com.yoh.backend.entity.Game;
import com.yoh.backend.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        return gameRepository.getGameByName(name) != null;
    }

    public Game getGameByName(String name) throws IllegalArgumentException{
        return gameRepository.getGameByName(name);
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

    public int getAllActiveGamesFilteredCount(String typeRegex, String regex, List<UUID> UUIDList, Boolean showDeleted) throws IllegalArgumentException{
        return gameRepository.getAllActiveGamesFilteredCount(typeRegex, regex, UUIDList, showDeleted);
    }

    public List<Game> getAllActiveGamesFiltered(String order, String typeRegex, String regex, List<UUID> UUIDList, int start, int limit, Boolean showDeleted) throws IllegalArgumentException{
        return gameRepository.getAllActiveGamesFiltered(order, typeRegex, regex, UUIDList, start, limit, showDeleted);
    }

    public List<Game> getAllGames() {
        return gameRepository.getAllGames("");
    }

    public List<Game> getAllGamesFiltered(String typeRegex, String order, String regex) {
        return gameRepository.getAllActiveGames(order, regex, typeRegex);
//        return unfilteredList;
//        if (!typeRegex.equals("")) {
//        return unfilteredList.stream().filter(i -> i.getType().toLowerCase().contains(typeRegex.toLowerCase()))
//                .collect(Collectors.toList());
//            }
//        else return unfilteredList;

    }
}
