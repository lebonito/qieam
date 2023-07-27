package com.bonito.qieam.games.service.impl;

import com.bonito.qieam.games.domain.Game;
import com.bonito.qieam.games.repository.GameRepository;
import com.bonito.qieam.games.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    private static final String ID_DO_NOT_EXISTS = "L'identifiant %d fourni n'existe pas.";

    @Override
    @Transactional
    public Game addGame(Game game) {
        if (game == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le jeu ne peut être null.");
        return gameRepository.save(game);
    }

    @Override
    public List<Game> findAllGames() {
        return gameRepository.findAll();
    }

    @Override
    public Game findGameById(Long id)  {
        return gameRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                String.format(ID_DO_NOT_EXISTS, id)));
    }


    @Override
    @Transactional
    public void deleteGameFromStoreById(Long id) {
        Game game = gameRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                String.format(ID_DO_NOT_EXISTS, id)));
        if (!game.isInStore())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Le jeu %d n'est pas dans le store.", id));
        game.setInStore(false);
        gameRepository.save(game);
    }
}
