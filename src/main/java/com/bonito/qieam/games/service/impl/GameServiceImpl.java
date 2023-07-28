package com.bonito.qieam.games.service.impl;

import com.bonito.qieam.games.domain.Game;
import com.bonito.qieam.games.repository.GameRepository;
import com.bonito.qieam.games.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    private static final String ID_DO_NOT_EXISTS = "L'identifiant %d fourni n'existe pas.";

    @Override
    @Transactional
    public Set<Game> addGames(Set<Game> games) {
        if (games.isEmpty() || games.contains(null))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le jeu ne peut être null.");
        return new HashSet<>(gameRepository.saveAll(games));
    }

    @Override
    @Transactional
    public Set<Game> addGamesToStore(Set<Game> games) {
        if (games.isEmpty() || games.contains(null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le jeu ne peut être null.");
        }

        if (!games.stream().anyMatch(game -> gameRepository.existsById(game.getId()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ajout impossible car un ou plusieurs jeux n'existe pas.");
        }
        games.forEach(game -> game.setInStore(true));
        return new HashSet<>(gameRepository.saveAll(games));
    }

    @Override
    public Set<Game> findAllGames() {
        return new HashSet<>(gameRepository.findAll());
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
