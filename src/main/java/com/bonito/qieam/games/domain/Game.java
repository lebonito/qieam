package com.bonito.qieam.games.domain;

import com.bonito.qieam.users.domain.Users;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Games")
@Table(name = "games")
public class Game implements Serializable {

    @Id @GeneratedValue
    private int id;
    @Column
    private String title;
    @Column
    private String cover;
    @ManyToMany(mappedBy = "games")
    private Set<Users> users;
}
