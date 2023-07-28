package com.bonito.qieam.games.service;

import com.bonito.qieam.games.domain.Game;

import java.util.List;
import java.util.Set;

public interface GameService {

    Set<Game> addGames(Set<Game> game);
    Set<Game> addGamesToStore(Set<Game> game);

    Set<Game> findAllGames();

    Game findGameById(Long id);

    void deleteGameFromStoreById(Long id);
}
