package com.club.management.square.repository;

import com.club.management.square.entity.SeniorShare;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 学长分享Repository
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface SeniorShareRepository extends JpaRepository<SeniorShare, Long>, JpaSpecificationExecutor<SeniorShare> {

    Page<SeniorShare> findByStatusOrderByIsTopDescCreateTimeDesc(Integer status, Pageable pageable);

    Page<SeniorShare> findByCategoryAndStatusOrderByIsTopDescCreateTimeDesc(Integer category, Integer status, Pageable pageable);

    Page<SeniorShare> findByAuthorIdOrderByCreateTimeDesc(Long authorId, Pageable pageable);

    @Modifying
    @Query("UPDATE SeniorShare s SET s.likeCount = s.likeCount + 1 WHERE s.id = :id")
    int increaseLikeCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE SeniorShare s SET s.likeCount = s.likeCount - 1 WHERE s.id = :id")
    int decreaseLikeCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE SeniorShare s SET s.commentCount = s.commentCount + 1 WHERE s.id = :id")
    int increaseCommentCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE SeniorShare s SET s.favoriteCount = s.favoriteCount + 1 WHERE s.id = :id")
    int increaseFavoriteCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE SeniorShare s SET s.viewCount = s.viewCount + 1 WHERE s.id = :id")
    int increaseViewCount(@Param("id") Long id);
}
