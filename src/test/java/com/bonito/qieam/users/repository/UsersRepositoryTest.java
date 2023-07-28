package com.bonito.qieam.users.repository;

import com.bonito.qieam.TestUtils;
import com.bonito.qieam.games.domain.Game;
import com.bonito.qieam.games.repository.GameRepository;
import com.bonito.qieam.users.domain.Users;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UsersRepositoryTest {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private GameRepository gameRepository;

    private EasyRandom easyRandom;
    @BeforeEach
    void beforeEach() {
        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.excludeField(FieldPredicates.named("id").and(FieldPredicates.inClass(Users.class)));
        parameters.collectionSizeRange(0, 2);
        parameters.setRandomizationDepth(2);
        parameters.excludeField(FieldPredicates.named("id").and(FieldPredicates.inClass(Game.class)));
        parameters.excludeField(FieldPredicates.named("friends").and(FieldPredicates.inClass(Users.class)));
        parameters.excludeField(FieldPredicates.named("friendsOf").and(FieldPredicates.inClass(Users.class)));
        easyRandom = new EasyRandom(parameters);
    }

    @Test
    void saveUser() {
        Users users = Users.builder().email("user1@quartz.fr").username("user1").build();
        Users save = usersRepository.save(users);
        Assertions.assertEquals(1, save.getId());
        Assertions.assertEquals("user1", save.getUsername());
        Assertions.assertEquals("user1@quartz.fr", save.getEmail());
    }

    @Test
    void getAllUsers() {
        Game game1 = Game.builder().title("title1").cover("cover1").build();
        Game game2 = Game.builder().title("title2").cover("cover2").build();
        List<Game> games = gameRepository.saveAll(List.of(game1, game2));
        Users user = Users.builder().username("user1").email("email1").games(new HashSet<>(games)).build();
        usersRepository.save(user);

        List<Users> all = usersRepository.findAll();
        Assertions.assertEquals(1, all.size());
        Assertions.assertEquals(2, all.get(0).getGames().size());
    }

    @Test
    void deletUser() {
        Game game1 = Game.builder().title("title1").cover("cover1").build();
        Game game2 = Game.builder().title("title2").cover("cover2").build();
        List<Game> games = gameRepository.saveAll(List.of(game1, game2));
        Users user = Users.builder().username("user1").email("email1").games(new HashSet<>(games)).build();
        usersRepository.delete(user);

        List<Users> all = usersRepository.findAll();
        Assertions.assertTrue(all.isEmpty());
    }
}
