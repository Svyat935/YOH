package com.yoh.backend.service;

import com.yoh.backend.entity.*;
import com.yoh.backend.enums.GamePatientStatus;
import com.yoh.backend.repository.GamePatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GamePatientService {

    @Autowired
    private GamePatientRepository gamePatientRepository;

    public void createGamePatient(GamePatient gamePatient) throws IllegalArgumentException{
        // TODO Добоавить валидацию и проверку на существование

        checksExistGamePatient(gamePatient);
        gamePatientRepository.createGamePatient(gamePatient);
    }

    public void saveGamePatient(GamePatient gamePatient) throws IllegalArgumentException{
        gamePatientRepository.createGamePatient(gamePatient);
    }

//    public void deleteGamePatient(GamePatient gamePatient) throws IllegalArgumentException{
//        gamePatientRepository.deleteGamePatient(gamePatient);
//    }

    private void checksExistGamePatient(GamePatient gamePatient) throws IllegalArgumentException{
        GamePatient gamePatient_copy = gamePatientRepository.getGamePatientByGameAndPatient(gamePatient.getGame(), gamePatient.getPatient());
        if (gamePatient_copy != null){
            throw new IllegalArgumentException("Sorry, but this game was already attached to this User");
        }
    }

    public GamePatient getGamePatientById(UUID id) throws IllegalArgumentException{
        GamePatient gamePatient = gamePatientRepository.getGamePatientByUUID(id);
        if (gamePatient != null) {
            return gamePatient;
        }
        else throw new IllegalArgumentException(
                String.format("Sorry, but GamePatient with this id (%s) wasn't found.", id)
        );
    }

    public List<Game> getAllGamesByPatient(Patient patient) throws IllegalArgumentException{
        List<GamePatient> gamePatientList = gamePatientRepository.getAllGamesPatientByPatient(patient, "");
        return gamePatientList
                .stream()
                .filter(i -> i.getGamePatientStatus().equals(GamePatientStatus.ACTIVE))
                .map(GamePatient::getGame)
                .collect(Collectors.toList());
    }

    public List<GamePatient> getActiveGamePatientByPatient(Patient patient, String order) throws IllegalArgumentException{
        return gamePatientRepository.getAllGamesPatientByPatient(patient, order)
                .stream()
                .filter(i -> i.getGamePatientStatus().equals(GamePatientStatus.ACTIVE))
                .collect(Collectors.toList());
    }

    public List<Patient> getAllPatientsByGame(Game game) throws IllegalArgumentException{
        List<GamePatient> gamePatientList = gamePatientRepository.getAllPatientByGame(game);
        return gamePatientList.stream().map(GamePatient::getPatient).collect(Collectors.toList());
    }

    public List<GamePatient> getAllGamePatientsByPatient(Patient patient) throws IllegalArgumentException{
        return gamePatientRepository.getAllGamesPatientByPatient(patient, "1");
    }

    public List<GamePatient> getAllGamePatientsByGame(Game game) throws IllegalArgumentException{
        return gamePatientRepository.getAllPatientByGame(game);
    }


    public List<GamePatient> getAllGamePatient() {
        return gamePatientRepository.getAllGamePatients();
    }

    public GamePatient getGamePatientByGameAndPatient(Game game, Patient patient) throws IllegalArgumentException{
        GamePatient gamePatient = gamePatientRepository.getGamePatientByGameAndPatient(game, patient);
        if (gamePatient != null) {
            return gamePatient;
        }
        else throw new IllegalArgumentException("Sorry, but GamePatient was not found.");
//        return gamePatientRepository.getGamePatientByGameAndPatient(game, patient);
    }

}
