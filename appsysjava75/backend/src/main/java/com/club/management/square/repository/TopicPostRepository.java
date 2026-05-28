package com.club.management.square.repository;

import com.club.management.square.entity.TopicPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 话题帖子Repository
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface TopicPostRepository extends JpaRepository<TopicPost, Long>, JpaSpecificationExecutor<TopicPost> {

    Page<TopicPost> findByTopicIdAndStatusOrderByIsTopDescCreateTimeDesc(Long topicId, Integer status, Pageable pageable);

    Page<TopicPost> findByUserIdOrderByCreateTimeDesc(Long userId, Pageable pageable);

    @Modifying
    @Query("UPDATE TopicPost p SET p.likeCount = p.likeCount + 1 WHERE p.id = :id")
    int increaseLikeCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE TopicPost p SET p.likeCount = p.likeCount - 1 WHERE p.id = :id")
    int decreaseLikeCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE TopicPost p SET p.commentCount = p.commentCount + 1 WHERE p.id = :id")
    int increaseCommentCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE TopicPost p SET p.viewCount = p.viewCount + 1 WHERE p.id = :id")
    int increaseViewCount(@Param("id") Long id);
}
