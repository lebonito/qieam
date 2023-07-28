package com.bonito.qieam.games.service;

import com.bonito.qieam.games.domain.Game;
import com.bonito.qieam.games.repository.GameRepository;
import com.bonito.qieam.games.service.impl.GameServiceImpl;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameServiceImpl gameService;

    @Test
    void addGames() {
        Set<Game> games = new EasyRandom().objects(Game.class, 3).collect(Collectors.toSet());
        when(gameRepository.saveAll(anySet())).thenReturn(games.stream().toList());
        gameService.addGames(games);
        verify(gameRepository, times(1)).saveAll(games);
    }

    @Test
    void addEmptySetOfGame() {
        HashSet<Game> set = new HashSet<>();
        ResponseStatusException responseStatusException = Assertions.assertThrows(ResponseStatusException.class,
                () -> gameService.addGames(set));
        Assertions.assertEquals("Le jeu ne peut être null.", responseStatusException.getReason());
    }

    @Test
    void addSetOfGameWithNullGame() {
        HashSet<Game> set = new HashSet<>();
        set.add(null);
        ResponseStatusException responseStatusException = Assertions.assertThrows(ResponseStatusException.class,
                () -> gameService.addGames(set));
        Assertions.assertEquals("Le jeu ne peut être null.", responseStatusException.getReason());
    }

    @Test
    void addGamesToStore() {
        Set<Game> games = new EasyRandom().objects(Game.class, 3).collect(Collectors.toSet());
        when(gameRepository.saveAll(anySet())).thenReturn(games.stream().toList());
        when(gameRepository.existsById(anyLong())).thenReturn(true);
        gameService.addGamesToStore(games);
        verify(gameRepository, times(1)).saveAll(games);
    }

    @Test
    void findAllGames() {
        List<Game> gameList = new EasyRandom().objects(Game.class, 5).toList();
        when(gameRepository.findAll()).thenReturn(gameList);
        gameService.findAllGames();
        verify(gameRepository, times(1)).findAll();
    }

    @Test
    void findGameById() {
        Game game = new EasyRandom().nextObject(Game.class);
        when(gameRepository.findById(anyLong())).thenReturn(Optional.ofNullable(game));
        gameService.findGameById(2L);
        verify(gameRepository, times(1)).findById(2L);
    }

    @Test
    void findGameByIdWithBadId() {
        ResponseStatusException responseStatusException = Assertions.assertThrows(ResponseStatusException.class,
                () -> gameService.findGameById(1L));
        Assertions.assertEquals("L'identifiant 1 fourni n'existe pas.", responseStatusException.getReason());
    }

    @Test
    void deleteGameFromStoreById() {
        Game game = new EasyRandom().nextObject(Game.class);
        game.setInStore(true);
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));
        gameService.deleteGameFromStoreById(1L);
        verify(gameRepository, times(1)).save(game);
    }

    @Test
    void deleteGameFromStoreByIdWithBadId() {
        ResponseStatusException responseStatusException = Assertions.assertThrows(ResponseStatusException.class,
                () -> gameService.deleteGameFromStoreById(1L));
        Assertions.assertEquals("L'identifiant 1 fourni n'existe pas.", responseStatusException.getReason());
    }

    @Test
    void deleteGameFromStoreByIdWithGameNotInStore() {
        Game game = new EasyRandom().nextObject(Game.class);
        game.setInStore(false);
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));
        ResponseStatusException responseStatusException = Assertions.assertThrows(ResponseStatusException.class,
                () -> gameService.deleteGameFromStoreById(1L));
        Assertions.assertEquals("Le jeu 1 n'est pas dans le store.", responseStatusException.getReason());
    }
}
