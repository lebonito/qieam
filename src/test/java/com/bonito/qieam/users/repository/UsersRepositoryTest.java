package com.bonito.qieam.users.repository;

import com.bonito.qieam.games.domain.Game;
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

import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UsersRepositoryTest {

    @Autowired
    private UsersRepository usersRepository;

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
        List<Users> usersList = easyRandom.objects(Users.class, 5).toList();
        usersRepository.saveAll(usersList);

        List<Users> all = usersRepository.findAll();
        Assertions.assertEquals(5, all.size());
        Assertions.assertTrue(all.containsAll(usersList));
    }

    @Test
    void deletUser() {
        List<Users> usersList = easyRandom.objects(Users.class, 4).toList();
        usersRepository.saveAll(usersList);

        usersRepository.delete(usersList.get(1));
        List<Users> all = usersRepository.findAll();
        Assertions.assertEquals(3, all.size());
        Assertions.assertTrue(usersList.containsAll(all));
    }
}
