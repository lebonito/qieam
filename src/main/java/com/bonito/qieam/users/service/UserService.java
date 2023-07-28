package com.bonito.qieam.users.service;

import com.bonito.qieam.games.domain.Game;
import com.bonito.qieam.users.domain.Users;

import java.util.List;
import java.util.Set;

public interface UserService {

    Set<Users> addUsers(Set<Users> usersSet);

    Users addFriends(Long idUser, Set<Users> friends);

    Users addGames(Long idUser, Set<Game> games);

    Set<Users> findAllUser();

    Users findUserById(Long id);

    Users findAllGamesFromUserById(Long id);

    Users findAllFriendsFromUserById(Long id);

    void deleteUserById(Long id);

    void removeFriendFromUserById(Long idUser, Long idFriend);

    void removeGameFromUserById(Long idUser, Long idGame);
}
