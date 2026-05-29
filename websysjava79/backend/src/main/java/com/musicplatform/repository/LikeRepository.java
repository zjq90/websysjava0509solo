package com.musicplatform.repository;

import com.musicplatform.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUser_IdAndTargetTypeAndTargetId(
            Long userId, Like.TargetType targetType, Long targetId);

    boolean existsByUser_IdAndTargetTypeAndTargetId(
            Long userId, Like.TargetType targetType, Long targetId);

    long countByTargetTypeAndTargetId(Like.TargetType targetType, Long targetId);

    void deleteByUser_IdAndTargetTypeAndTargetId(
            Long userId, Like.TargetType targetType, Long targetId);
}
