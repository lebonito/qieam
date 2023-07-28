package com.bonito.qieam.users.repository;

import com.bonito.qieam.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    @Query(value = "select u from Users u join fetch u.games where u.id = :id")
    Optional<Users> findAllGamesFromUserId(Long id);

    @Query(value = "select u from Users u join fetch u.friends where u.id = :id")
    Optional<Users> findAllFriendsFromUserId(Long id);
}
