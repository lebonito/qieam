package com.bonito.qieam.games.service;

import com.bonito.qieam.games.domain.Game;

import java.util.List;

public interface GameService {

    Game addGame(Game game);

    List<Game> findAllGames();

    Game findGameById(Long id);

    void deleteGameFromStoreById(Long id);
}
