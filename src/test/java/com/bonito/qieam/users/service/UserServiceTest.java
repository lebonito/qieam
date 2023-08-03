package com.bonito.qieam.users.service;

import com.bonito.qieam.games.domain.Game;
import com.bonito.qieam.games.repository.GameRepository;
import com.bonito.qieam.users.domain.Users;
import com.bonito.qieam.users.repository.UsersRepository;
import com.bonito.qieam.users.service.impl.UserServiceImpl;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void addUsers() {
        Set<Users> users = new EasyRandom().objects(Users.class, 6).collect(Collectors.toSet());
        when(usersRepository.saveAll(anySet())).thenReturn(users.stream().toList());
        when(usersRepository.existsById(anyLong())).thenReturn(true);
        when(gameRepository.existsById(anyLong())).thenReturn(true);
        userService.addUsers(users);
        verify(usersRepository, times(1)).saveAll(users);
    }

    @Test
    void addSetOfNullUser() {
        HashSet<Users> set = new HashSet<>();
        set.add(null);
        ResponseStatusException responseStatusException = Assertions.assertThrows(ResponseStatusException.class,
                () -> userService.addUsers(set));
        Assertions.assertEquals("L'utilisateur ne peut être null.", responseStatusException.getReason());
    }

    @Test
    void addEmptySetOfUser() {
        Set<Users> objects = Set.of();
        ResponseStatusException responseStatusException = Assertions.assertThrows(ResponseStatusException.class,
                () -> userService.addUsers(objects));
        Assertions.assertEquals("L'utilisateur ne peut être null.", responseStatusException.getReason());
    }

    @Test
    void getAllUsers() {
        Set<Users> users = new EasyRandom().objects(Users.class, 5).collect(Collectors.toSet());
        when(usersRepository.findAll()).thenReturn(users.stream().toList());
        userService.findAllUser();
        verify(usersRepository, times(1)).findAll();
    }

    @Test
    void getUserById() {
        Users users = new EasyRandom().nextObject(Users.class);
        when(usersRepository.findById(anyLong())).thenReturn(Optional.ofNullable(users));
        userService.findUserById(2L);
        verify(usersRepository, times(1)).findById(2L);
    }

    @Test
    void getUserByIdWithBadId() {
        ResponseStatusException responseStatusException = Assertions.assertThrows(ResponseStatusException.class,
                () -> userService.findUserById(2L));
        Assertions.assertEquals("L'identifiant 2 n'existe pas.", responseStatusException.getReason());
    }

    @Test
    void deleteUser() {
        when(usersRepository.existsById(anyLong())).thenReturn(true);
        userService.deleteUserById(2L);
        verify(usersRepository, times(1)).existsById(2L);
        verify(usersRepository, times(1)).deleteById(2L);
    }


    @Test
    void getAllFriendsFromUser() {
        Users users = new EasyRandom().nextObject(Users.class);
        when(usersRepository.existsById(anyLong())).thenReturn(true);
        when(usersRepository.findAllFriendsFromUserId(anyLong())).thenReturn(Optional.ofNullable(users));
        userService.findAllFriendsFromUserById(2L);
        verify(usersRepository, times(1)).findAllFriendsFromUserId(2L);
    }

    @Test
    void getAllFriendsFromUserWithBadId() {
        ResponseStatusException responseStatusException = Assertions.assertThrows(ResponseStatusException.class,
                () -> userService.findAllFriendsFromUserById(2L));
        Assertions.assertEquals("L'identifiant 2 n'existe pas.", responseStatusException.getReason());
    }

    @Test
    void getAllGamesFromUser() {
        Users users = new EasyRandom().nextObject(Users.class);
        when(usersRepository.existsById(anyLong())).thenReturn(true);
        when(usersRepository.findAllGamesFromUserId(anyLong())).thenReturn(Optional.ofNullable(users));
        userService.findAllGamesFromUserById(2L);
        verify(usersRepository, times(1)).findAllGamesFromUserId(2L);
    }

    @Test
    void getAllGamesFromUserWithBadId() {
        ResponseStatusException responseStatusException = Assertions.assertThrows(ResponseStatusException.class,
                () -> userService.findAllGamesFromUserById(2L));
        Assertions.assertEquals("L'identifiant 2 n'existe pas.", responseStatusException.getReason());
    }

    @Test
    void addGamesToUser() {
        Users user = new EasyRandom().nextObject(Users.class);
        Set<Game> gameList = new EasyRandom().objects(Game.class,4).collect(Collectors.toSet());
        when(usersRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(usersRepository.save(any(Users.class))).thenReturn(user);
        when(gameRepository.existsById(anyLong())).thenReturn(true);
        userService.addGames(1L, gameList);
        verify(usersRepository, times(1)).save(user);
    }

    @Test
    void addGamesToUserWithBadId() {
        Set<Game> gameList = new EasyRandom().objects(Game.class,4).collect(Collectors.toSet());
        ResponseStatusException responseStatusException = Assertions.assertThrows(ResponseStatusException.class,
                () -> userService.addGames(1L, gameList));
        Assertions.assertEquals("L'identifiant 1 de l'utilisateur n'existe pas.", responseStatusException.getReason());
    }

    @Test
    void addFriendsToUserWithBadId() {
        Set<Users> users = new EasyRandom().objects(Users.class,4).collect(Collectors.toSet());
        ResponseStatusException responseStatusException = Assertions.assertThrows(ResponseStatusException.class,
                () -> userService.addFriends(1L, users));
        Assertions.assertEquals("L'identifiant 1 de l'utilisateur n'existe pas.", responseStatusException.getReason());
    }

    @Test
    void removeFriendFromUser() {
        Users user = new EasyRandom().nextObject(Users.class);
        AtomicLong aLong = new AtomicLong(0L);
        user.getFriends().forEach(users -> {
            if (aLong.get() < user.getFriends().size()) {
                users.setId(aLong.get());
                aLong.getAndIncrement();
            }
        });

        when(usersRepository.findById(anyLong())).thenReturn(Optional.of(user));
        userService.removeFriendFromUserById(2L, 1L);
        verify(usersRepository, times(1)).findById(1L);
        verify(usersRepository, times(1)).save(user);
    }

    @Test
    void removeFriendFromUserWithBadFriendId() {
        Users user = new EasyRandom().nextObject(Users.class);

        when(usersRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user))
                .thenThrow(ResponseStatusException.class);
        ResponseStatusException responseStatusException = Assertions.assertThrows(ResponseStatusException.class,
                () -> userService.removeFriendFromUserById(1L, 1L));
        Assertions.assertEquals("L'identifiant 1 de l'ami n'existe pas.", responseStatusException.getReason());
    }

    @Test
    void removeFriendFromUserWithBadUserId() {
        ResponseStatusException responseStatusException = Assertions.assertThrows(ResponseStatusException.class,
                () -> userService.removeFriendFromUserById(1L, 1L));
        Assertions.assertEquals("L'identifiant 1 de l'utilisateur n'existe pas.", responseStatusException.getReason());
    }

    @Test
    void removeGameFromUser() {
        Users user = new EasyRandom().nextObject(Users.class);
        Game game = new EasyRandom().nextObject(Game.class);
        AtomicLong aLong = new AtomicLong(0L);
        user.getGames().forEach(game1 -> {
            if (aLong.get() < user.getGames().size()) {
                game1.setId(aLong.get());
                aLong.getAndIncrement();
            }
        });

        when(usersRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));
        userService.removeGameFromUserById(1L, 2L);
        verify(gameRepository, times(1)).findById(2L);
        verify(usersRepository, times(1)).save(user);
    }

    @Test
    void removeGameFromUserWithBadUserId() {
        ResponseStatusException responseStatusException = Assertions.assertThrows(ResponseStatusException.class,
                () -> userService.removeGameFromUserById(1L, 2L));
        Assertions.assertEquals("L'identifiant 1 de l'utilisateur n'existe pas.", responseStatusException.getReason());
    }

    @Test
    void removeGameFromUserWithBadGameId() {
        Users user = new EasyRandom().nextObject(Users.class);
        when(usersRepository.findById(anyLong())).thenReturn(Optional.of(user));
        ResponseStatusException responseStatusException = Assertions.assertThrows(ResponseStatusException.class,
                () -> userService.removeGameFromUserById(1L, 2L));
        Assertions.assertEquals("L'identifiant 2 du jeu n'existe pas.", responseStatusException.getReason());
    }
}
