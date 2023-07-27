package com.bonito.qieam.games.repository;

import com.bonito.qieam.games.domain.Game;
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
class GameRepositoryTest {

    @Autowired
    private GameRepository gameRepository;

    private EasyRandom easyRandom;
    @BeforeEach
    void beforeEach() {
        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.excludeField(FieldPredicates.named("id").and(FieldPredicates.inClass(Game.class)));
        parameters.excludeField(FieldPredicates.named("users").and(FieldPredicates.inClass(Game.class)));
        easyRandom = new EasyRandom(parameters);
    }

    @Test
    void saveGame() {
        Game game = easyRandom.nextObject(Game.class);
        Game save = gameRepository.save(game);

        Assertions.assertNotNull(save);
        Assertions.assertEquals(1, save.getId());
        Assertions.assertEquals(game.getTitle(), save.getTitle());
        Assertions.assertEquals(game.getCover(), save.getCover());
    }

    @Test
    void getAllGames() {
        List<Game> gameList = easyRandom.objects(Game.class, 4).toList();
        gameRepository.saveAll(gameList);

        List<Game> all = gameRepository.findAll();
        Assertions.assertEquals(4, all.size());
        Assertions.assertTrue(all.containsAll(gameList));
    }

    @Test
    void deleteGame() {
        List<Game> gameList = easyRandom.objects(Game.class, 3).toList();
        gameRepository.saveAll(gameList);

        gameRepository.delete(gameList.get(1));

        List<Game> all = gameRepository.findAll();
        Assertions.assertEquals(2, all.size());
        Assertions.assertTrue(gameList.containsAll(all));
    }
}
