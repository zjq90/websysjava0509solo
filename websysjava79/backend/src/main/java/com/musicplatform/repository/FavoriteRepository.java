package com.musicplatform.repository;

import com.musicplatform.entity.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByUser_IdAndTargetTypeAndTargetId(
            Long userId, Favorite.TargetType targetType, Long targetId);

    boolean existsByUser_IdAndTargetTypeAndTargetId(
            Long userId, Favorite.TargetType targetType, Long targetId);

    Page<Favorite> findByUser_IdAndTargetType(Long userId, Favorite.TargetType targetType, Pageable pageable);

    long countByTargetTypeAndTargetId(Favorite.TargetType targetType, Long targetId);

    void deleteByUser_IdAndTargetTypeAndTargetId(
            Long userId, Favorite.TargetType targetType, Long targetId);
}
