package com.club.repository;

import com.club.entity.ClubPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 社团讨论区帖子Repository接口
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface ClubPostRepository extends JpaRepository<ClubPost, Long>, JpaSpecificationExecutor<ClubPost> {

    Page<ClubPost> findByClubIdAndStatusAndDeletedFalseOrderByTopDescCreateTimeDesc(Long clubId, Integer status, Pageable pageable);

    Page<ClubPost> findByClubIdAndTypeAndStatusAndDeletedFalseOrderByTopDescCreateTimeDesc(Long clubId, String type, Integer status, Pageable pageable);

    Page<ClubPost> findByUserIdAndStatusAndDeletedFalseOrderByCreateTimeDesc(Long userId, Integer status, Pageable pageable);

    Optional<ClubPost> findByIdAndDeletedFalse(Long id);

    @Modifying
    @Query("UPDATE ClubPost p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
    void incrementViewCount(@Param("postId") Long postId);

    @Modifying
    @Query("UPDATE ClubPost p SET p.likeCount = p.likeCount + 1 WHERE p.id = :postId")
    void incrementLikeCount(@Param("postId") Long postId);

    @Modifying
    @Query("UPDATE ClubPost p SET p.commentCount = p.commentCount + 1 WHERE p.id = :postId")
    void incrementCommentCount(@Param("postId") Long postId);
}
