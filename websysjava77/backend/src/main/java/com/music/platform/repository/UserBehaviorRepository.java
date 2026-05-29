package com.music.platform.repository;

import com.music.platform.entity.UserBehavior;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface UserBehaviorRepository extends JpaRepository<UserBehavior, Long> {
    List<UserBehavior> findByUserIdAndType(Long userId, String type);
    List<UserBehavior> findByUserIdOrderByCreatedAtDesc(Long userId);
    boolean existsByUserIdAndMusicIdAndType(Long userId, Long musicId, String type);
    void deleteByUserIdAndMusicIdAndType(Long userId, Long musicId, String type);

    @Query("SELECT ub.musicId, COUNT(ub) as cnt FROM UserBehavior ub WHERE ub.type = :type " +
           "GROUP BY ub.musicId ORDER BY cnt DESC")
    List<Object[]> findTopMusicByType(@Param("type") String type);

    @Query("SELECT ub.musicId FROM UserBehavior ub WHERE ub.userId = :userId AND ub.type = 'play' " +
           "GROUP BY ub.musicId ORDER BY MAX(ub.createdAt) DESC")
    List<Long> findRecentPlayedMusicIds(@Param("userId") Long userId);

    @Query("SELECT DISTINCT ub.musicId FROM UserBehavior ub WHERE ub.userId = :userId AND ub.type = 'like'")
    List<Long> findLikedMusicIds(@Param("userId") Long userId);

    @Query("SELECT DISTINCT ub.musicId FROM UserBehavior ub WHERE ub.userId = :userId AND ub.type = 'collect'")
    List<Long> findCollectedMusicIds(@Param("userId") Long userId);
}
