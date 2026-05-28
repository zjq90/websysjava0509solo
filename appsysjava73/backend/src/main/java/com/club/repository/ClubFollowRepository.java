package com.club.repository;

import com.club.entity.ClubFollow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 社团关注Repository接口
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface ClubFollowRepository extends JpaRepository<ClubFollow, Long>, JpaSpecificationExecutor<ClubFollow> {

    Page<ClubFollow> findByUserIdAndDeletedFalseOrderByCreateTimeDesc(Long userId, Pageable pageable);

    List<ClubFollow> findByUserIdAndDeletedFalse(Long userId);

    List<ClubFollow> findByClubIdAndDeletedFalse(Long clubId);

    Optional<ClubFollow> findByUserIdAndClubIdAndDeletedFalse(Long userId, Long clubId);

    boolean existsByUserIdAndClubIdAndDeletedFalse(Long userId, Long clubId);

    long countByClubIdAndDeletedFalse(Long clubId);
}
