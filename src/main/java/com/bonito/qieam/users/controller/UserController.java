package com.bonito.qieam.users.controller;

import com.bonito.qieam.games.domain.Game;
import com.bonito.qieam.games.dto.GameDto;
import com.bonito.qieam.users.domain.Users;
import com.bonito.qieam.users.dto.UserDto;
import com.bonito.qieam.users.service.UserService;
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
@RequestMapping(path = "api/users")
public class UserController {

    private final UserService userService;

    private final Mapper<UserDto, Users> mapperUser;

    private final Mapper<GameDto, Game> mapperGame;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Set<UserDto>> addUsers(@RequestBody Set<UserDto> userDtoSet) {
        Set<Users> usersSet = userDtoSet.stream().map(userDto -> mapperUser.convertToEntity(userDto, Users.class))
                .collect(Collectors.toSet());
        Set<UserDto> userDtos = userService.addUsers(usersSet)
                .stream().map(users -> mapperUser.convertToDTO(users, UserDto.class)).collect(Collectors.toSet());
        return ResponseEntity.status(HttpStatus.CREATED).body(userDtos);
    }

    @PostMapping(path = "/{userId}/games", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserDto> addGamesToUser(@PathVariable("userId") Long userId, Set<GameDto> gameSet) {
        Set<Game> games = gameSet.stream().map(gameDto -> mapperGame.convertToEntity(gameDto, Game.class)).collect(Collectors.toSet());
        UserDto userDto = mapperUser.convertToDTO(userService.addGames(userId, games), UserDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @PostMapping(path = "/{userId}/friends", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserDto> addFriendsToUser(@PathVariable("userId") Long userId, Set<UserDto> friendSet) {
        Set<Users> friends = friendSet.stream().map(userDto -> mapperUser.convertToEntity(userDto, Users.class))
                .collect(Collectors.toSet());
        UserDto userDto = mapperUser.convertToDTO(userService.addFriends(userId, friends), UserDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Set<UserDto>> getAllUsers() {
        Set<UserDto> userDtoSet = userService.findAllUser().stream()
                .map(users -> mapperUser.convertToDTO(users, UserDto.class)).collect(Collectors.toSet());
        return ResponseEntity.ok(userDtoSet);
    }

    @GetMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserDto> getUserById(@PathVariable("userId") Long userId) {
        UserDto userDto = mapperUser.convertToDTO(userService.findUserById(userId), UserDto.class);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping(path = "/{userId}/games", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserDto> getAllGamesFromUserById(@PathVariable("userId") Long userId) {
        UserDto userDto = mapperUser.convertToDTO(userService.findAllGamesFromUserById(userId), UserDto.class);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping(path = "/{userId}/friends", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserDto> getAllFriendsFromUserById(@PathVariable("userId") Long userId) {
        UserDto userDto = mapperUser.convertToDTO(userService.findAllFriendsFromUserById(userId), UserDto.class);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> deleteUserById(@PathVariable("userId") Long userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.ok("Suppression de l'utilisateur réussie!");
    }

    @DeleteMapping(path = "/{userId}/games/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> deleteGameByUserIdAndGameId(@PathVariable("userId") Long userId, @PathVariable("gameId") Long gameId) {
        userService.removeGameFromUserById(userId, gameId);
        return ResponseEntity.ok("Suppression du jeu de l'utilisateur réussie!");
    }

    @DeleteMapping(path = "/{userId}/friends/{friendId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> deleteFriendByUserIdAndFiendId(@PathVariable("userId") Long userId, @PathVariable("friendId") Long friendId) {
        userService.removeFriendFromUserById(userId, friendId);
        return ResponseEntity.ok("Suppression de l'ami réussie!");
    }
}
