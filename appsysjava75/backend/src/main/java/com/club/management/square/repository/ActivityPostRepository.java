package com.club.management.square.repository;

import com.club.management.square.entity.ActivityPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 活动圈动态Repository
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface ActivityPostRepository extends JpaRepository<ActivityPost, Long>, JpaSpecificationExecutor<ActivityPost> {

    Page<ActivityPost> findByStatusOrderByCreateTimeDesc(Integer status, Pageable pageable);

    Page<ActivityPost> findByUserIdOrderByCreateTimeDesc(Long userId, Pageable pageable);

    Page<ActivityPost> findByClubIdOrderByCreateTimeDesc(Long clubId, Pageable pageable);

    @Modifying
    @Query("UPDATE ActivityPost p SET p.likeCount = p.likeCount + 1 WHERE p.id = :id")
    int increaseLikeCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE ActivityPost p SET p.likeCount = p.likeCount - 1 WHERE p.id = :id")
    int decreaseLikeCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE ActivityPost p SET p.commentCount = p.commentCount + 1 WHERE p.id = :id")
    int increaseCommentCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE ActivityPost p SET p.viewCount = p.viewCount + 1 WHERE p.id = :id")
    int increaseViewCount(@Param("id") Long id);
}
