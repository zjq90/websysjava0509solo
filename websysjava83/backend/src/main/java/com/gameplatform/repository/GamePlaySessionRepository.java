package com.gameplatform.repository;

import com.gameplatform.entity.GamePlaySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GamePlaySessionRepository extends JpaRepository<GamePlaySession, Long> {
    @Query("SELECT COUNT(gps) FROM GamePlaySession gps WHERE gps.startTime >= :start AND gps.startTime <= :end")
    Long countSessionsBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT AVG(gps.durationSeconds) FROM GamePlaySession gps WHERE gps.startTime >= :start AND gps.startTime <= :end AND gps.durationSeconds IS NOT NULL")
    Double getAverageDurationBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = "SELECT gps.game_id, g.name, COUNT(gps.id) as cnt " +
           "FROM game_play_sessions gps INNER JOIN games g ON gps.game_id = g.id " +
           "WHERE gps.start_time >= :start AND gps.start_time <= :end " +
           "GROUP BY gps.game_id, g.name ORDER BY cnt DESC", nativeQuery = true)
    List<Object[]> getTopGames(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = "SELECT CAST(start_time AS DATE) as dt, COUNT(id) as cnt " +
           "FROM game_play_sessions WHERE game_id = :gameId AND start_time >= :start AND start_time <= :end " +
           "GROUP BY CAST(start_time AS DATE) ORDER BY dt", nativeQuery = true)
    List<Object[]> getLaunchTrendByGame(@Param("gameId") Long gameId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT gps.sourceChannel, COUNT(gps) FROM GamePlaySession gps " +
           "WHERE gps.gameId = :gameId AND gps.startTime >= :start AND gps.startTime <= :end " +
           "GROUP BY gps.sourceChannel")
    List<Object[]> getSourceChannelDistribution(@Param("gameId") Long gameId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = "SELECT AVG(CASE WHEN ad_clicked = true THEN 1.0 ELSE 0.0 END) FROM game_play_sessions " +
           "WHERE game_id = :gameId AND start_time >= :start AND start_time <= :end", nativeQuery = true)
    Double getAdClickRate(@Param("gameId") Long gameId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = "SELECT AVG(CASE WHEN purchase_made = true THEN 1.0 ELSE 0.0 END) FROM game_play_sessions " +
           "WHERE game_id = :gameId AND start_time >= :start AND start_time <= :end", nativeQuery = true)
    Double getPurchaseConversionRate(@Param("gameId") Long gameId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
