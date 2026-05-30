package com.gameplatform.repository;

import com.gameplatform.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findByCode(String code);
    List<Game> findByEnabled(Boolean enabled);
    List<Game> findByRewardVideoEnabled(Boolean enabled);
}
