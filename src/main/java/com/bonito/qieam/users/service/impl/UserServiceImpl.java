package com.bonito.qieam.users.service.impl;

import com.bonito.qieam.games.domain.Game;
import com.bonito.qieam.games.repository.GameRepository;
import com.bonito.qieam.users.domain.Users;
import com.bonito.qieam.users.repository.UsersRepository;
import com.bonito.qieam.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;

    private final GameRepository gameRepository;

    private static final String ID_DO_NOT_EXISTS = "L'identifiant %d n'existe pas.";
    private static final String USER_ID_DO_NOT_EXISTS = "L'identifiant %d de l'utilisateur n'existe pas.";

    @Override
    public Users addUser(Users user) {
        if (user == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'utilisateur ne peut être null.");
        return usersRepository.save(user);
    }

    @Override
    public Users addFriends(Long idUser, List<Users> friends) {
        Users user = usersRepository.findById(idUser).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                String.format(USER_ID_DO_NOT_EXISTS, idUser)));
        user.addFriend(friends);
        return usersRepository.save(user);
    }

    @Override
    public Users addGames(Long idUser, List<Game> games) {
        Users user = usersRepository.findById(idUser).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                String.format(USER_ID_DO_NOT_EXISTS, idUser)));
        user.addGames(games);
        return usersRepository.save(user);
    }

    @Override
    public List<Users> findAllUser() {
        return usersRepository.findAll();
    }

    @Override
    public Users findUserById(Long id) {
        return usersRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                String.format(ID_DO_NOT_EXISTS, id)));
    }

    @Override
    public void deleteUserById(Long id) {
        if(usersRepository.existsById(id)) {
            usersRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format(ID_DO_NOT_EXISTS, id));
        }
    }

    @Override
    public void removeFriendFromUserById(Long idUser, Long idFriend) {
        Users user = usersRepository.findById(idUser).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                String.format(USER_ID_DO_NOT_EXISTS, idUser)));
        if (user.getFriends().stream().anyMatch(users -> Objects.equals(users.getId(), idFriend))) {
            Users friend = usersRepository.findById(idFriend).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("L'identifiant %d de l'ami n'existe pas.", idFriend)));
            user.removeFriend(friend);
            usersRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("L'identifiant %d de l'ami n'existe pas.", idFriend));
        }
    }

    @Override
    public void removeGameFromUserById(Long idUser, Long idGame) {
        Users user = usersRepository.findById(idUser)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        String.format(USER_ID_DO_NOT_EXISTS, idUser)));
        if (user.getGames().stream().anyMatch(game -> Objects.equals(game.getId(), idGame))) {
            Game game = gameRepository.findById(idGame).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format(ID_DO_NOT_EXISTS, idGame)));
            user.removeGame(game);
            usersRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("L'identifiant %d du jeu n'existe pas.", idGame));
        }
    }
}
