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

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;

    private final GameRepository gameRepository;

    private static final String ID_DO_NOT_EXISTS = "L'identifiant %d n'existe pas.";
    private static final String USER_ID_DO_NOT_EXISTS = "L'identifiant %d de l'utilisateur n'existe pas.";

    @Override
    @Transactional
    public Set<Users> addUsers(Set<Users> usersSet) {
        if (usersSet.isEmpty() || usersSet.contains(null))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'utilisateur ne peut être null.");
        boolean anyGameMatch = usersSet.stream()
                .anyMatch(users -> users.getGames().stream()
                        .anyMatch(game -> !gameRepository.existsById(game.getId())));
        if (anyGameMatch) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Un ou plusieurs jeux n'existent pas.");
        }

        boolean anyFriendMatch = usersSet.stream()
                .anyMatch(users -> users.getFriends().stream()
                        .anyMatch(friend -> !usersRepository.existsById(friend.getId())));

        if (anyFriendMatch) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Un ou plusieurs amis n'existent pas.");
        }
        usersSet.forEach(users -> {
            Set<Game> games = users.getGames().stream()
                    .map(game -> gameRepository.findById(game.getId()).orElseThrow()).collect(Collectors.toSet());
            users.getGames().clear();
            users.getGames().addAll(games);
        });

        usersSet.forEach(users -> {
            Set<Users> friends = users.getFriends().stream()
                    .map(friend -> usersRepository.findById(friend.getId()).orElseThrow()).collect(Collectors.toSet());
            users.getFriends().clear();
            users.getFriends().addAll(friends);
        });
        return new HashSet<>(usersRepository.saveAll(usersSet));
    }

    @Override
    @Transactional
    public Users addFriends(Long idUser, Set<Users> friends) {
        Users user = usersRepository.findById(idUser).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                String.format(USER_ID_DO_NOT_EXISTS, idUser)));
        if (!friends.stream().allMatch(friend -> usersRepository.existsById(friend.getId()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ajout impossible car un ou plusieurs amis de la liste n'existe pas.");
        }
        if (friends.stream().anyMatch(friend -> user.getFriends().contains(friend))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ajout impossible car l'utilisateur possède déjà un ou plusieurs amis de la liste.");
        }
        user.addFriend(friends);

        return usersRepository.save(user);
    }

    @Override
    @Transactional
    public Users addGames(Long idUser, Set<Game> games) {
        Users user = usersRepository.findById(idUser).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                String.format(USER_ID_DO_NOT_EXISTS, idUser)));
        if (!games.stream().allMatch(game -> gameRepository.existsById(game.getId()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ajout impossible car un ou plusieurs jeux de la liste n'existe pas.");
        }
        if (games.stream().anyMatch(game -> user.getGames().contains(game))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ajout impossible car l'utilisateur possède déjà un ou plusieurs jeux de la liste.");
        }
        user.addGames(games);
        return usersRepository.save(user);
    }

    @Override
    public Set<Users> findAllUser() {
        List<Users> all = usersRepository.findAll();
        return new HashSet<>(all);
    }

    @Override
    public Users findUserById(Long id) {
        return usersRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                String.format(ID_DO_NOT_EXISTS, id)));
    }

    @Override
    public Users findAllGamesFromUserById(Long id) {
        if (!usersRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(ID_DO_NOT_EXISTS, id));
        }
        return usersRepository.findAllGamesFromUserId(id).orElseThrow();
    }

    @Override
    public Users findAllFriendsFromUserById(Long id) {
        if (!usersRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(ID_DO_NOT_EXISTS, id));
        }
        return usersRepository.findAllFriendsFromUserId(id).orElseThrow();
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        if(usersRepository.existsById(id)) {
            usersRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format(ID_DO_NOT_EXISTS, id));
        }
    }

    @Override
    @Transactional
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
    @Transactional
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
