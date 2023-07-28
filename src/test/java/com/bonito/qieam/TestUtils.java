package com.bonito.qieam;

import com.bonito.qieam.games.domain.Game;
import com.bonito.qieam.games.dto.GameDto;
import com.bonito.qieam.users.domain.Users;
import com.bonito.qieam.users.dto.UserDto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestUtils {

    public static String userDtoToString() {
        return """
                [
                     {
                         "username": "user 1",
                         "email": "email.user1",
                         "games": [
                             {
                                 "id": 3,
                                 "title": "jeu 4",
                                 "cover": "cover jeu 4"
                             },
                             {
                                 "id": 5,
                                 "title": "jeu 2",
                                 "cover": "cover jeu 2"
                             }
                         ]
                     }
                ]
                """;
    }

    public static String gameSetToString() {
        return """
                [
                     {
                         "id": 3,
                         "title": "jeu 4",
                         "cover": "cover jeu 4"
                     }
                ]
                """;
    }

    public static Game game1 = Game.builder().id(3L).title("jeu 4").cover("cover jeu 4").build();

    public static GameDto game1Dto = GameDto.builder().id(3L).title("jeu 4").cover("cover jeu 4").build();

    public static Game game2 = Game.builder().id(5L).title("jeu 2").cover("cover jeu 2").build();

    public static GameDto game2Dto = GameDto.builder().id(5L).title("jeu 2").cover("cover jeu 2").build();

    public static Users user() {
        HashSet<Game> set = new HashSet<>(List.of(game1, game2));
        return Users.builder().id(1L).username("user 1").email("email.user1").games(set).build();
    }

    public static Set<Users> usersSet() {
        HashSet<Game> set = new HashSet<>(List.of(game1, game2));
        Users users = Users.builder().id(1L).username("user 1").email("email.user1").games(set).build();
        return new HashSet<>(List.of(users));
    }

    public static UserDto userDto() {
        HashSet<GameDto> set = new HashSet<>(List.of(game1Dto, game2Dto));
        return UserDto.builder().id(1L).username("user 1").email("email.user1").games(set).build();
    }
    public static Set<UserDto> userDtoSet() {
        HashSet<GameDto> set = new HashSet<>(List.of(game1Dto, game2Dto));
        UserDto userDto1 = UserDto.builder().id(1L).username("user 1").email("email.user1").games(set).build();
        return new HashSet<>(List.of(userDto1));
    }

}
