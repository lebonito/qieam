package com.bonito.qieam.games.controller;

import com.bonito.qieam.games.domain.Game;
import com.bonito.qieam.games.dto.GameDto;
import com.bonito.qieam.games.service.GameService;
import com.bonito.qieam.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/games")
public class GameController {

    private final GameService gameService;

    private final Mapper<GameDto, Game> mapper;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<GameDto>> addGames(@RequestBody Set<GameDto> gameDtoSet) {
        Set<Game> games = gameDtoSet.stream().map(gameDto -> mapper.convertToEntity(gameDto, Game.class))
                .collect(Collectors.toSet());
        Set<GameDto> gameDtos = gameService.addGames(games).stream()
                .map(game -> mapper.convertToDTO(game, GameDto.class)).collect(Collectors.toSet());
        return ResponseEntity.status(HttpStatus.CREATED).body(gameDtos);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<GameDto>> getAllGames() {
        Set<GameDto> gameDtos = gameService.findAllGames().stream()
                .map(game -> mapper.convertToDTO(game, GameDto.class)).collect(Collectors.toSet());
        return ResponseEntity.ok(gameDtos);
    }

    @GetMapping(path = "/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameDto> getGameById(@PathVariable("gameId") Long gameId) {
        GameDto gameDto = mapper.convertToDTO(gameService.findGameById(gameId), GameDto.class);
        return ResponseEntity.ok(gameDto);
    }
}
