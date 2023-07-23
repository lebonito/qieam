package com.bonito.qieam.users.domain;

import com.bonito.qieam.games.domain.Game;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Builder
@Entity(name = "Users")
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class Users implements Serializable {

    @Id @GeneratedValue
    private int id;

    @Column
    private String username;

    @Column
    private String email;

    @ManyToMany
    @JoinTable(name = "users_games", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id"))
    private Set<Game> games;

    @ManyToMany
    @JoinTable(name = "users_friends", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"))
    private Set<Users> friends;

    @ManyToMany()
    @JoinTable(name = "users_friends", joinColumns = @JoinColumn(name = "friend_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<Users> friendsOf;
}
