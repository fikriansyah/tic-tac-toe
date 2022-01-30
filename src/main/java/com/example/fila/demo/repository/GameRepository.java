package com.example.fila.demo.repository;

import com.example.fila.demo.entity.Game;
import com.example.fila.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface GameRepository extends JpaRepository<Game, Long> {

    Game findFirstByUserOrderByIdDesc(User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM Game WHERE user = :user")
    void deleteUserGames(@Param("user") User user);

}
