package com.bonito.qieam.users.service;

import com.bonito.qieam.games.domain.Game;
import com.bonito.qieam.users.domain.Users;

import java.util.List;

public interface UserService {

    Users addUser(Users user);

    Users addFriends(Long idUser, List<Users> friends);

    Users addGames(Long idUser, List<Game> games);

    List<Users> findAllUser();

    Users findUserById(Long id);

    void deleteUserById(Long id);

    void removeFriendFromUserById(Long idUser, Long idFriend);

    void removeGameFromUserById(Long idUser, Long idGame);
}
