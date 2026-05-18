package com.heritage.repository;

import com.heritage.entity.CommunityPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 社区帖子数据访问接口
 *
 * @author Heritage Team
 * @version 1.0.0
 */
@Repository
public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {

    Page<CommunityPost> findByStatusAndAiReviewStatusOrderByCreatedTimeDesc(Integer status, Integer aiReviewStatus, Pageable pageable);

    List<CommunityPost> findByUserIdOrderByCreatedTimeDesc(Long userId);

    @Query("SELECT c FROM CommunityPost c WHERE c.title LIKE %?1% OR c.content LIKE %?1%")
    Page<CommunityPost> searchByKeyword(String keyword, Pageable pageable);

    List<CommunityPost> findTop10ByOrderByLikeCountDesc();

    Page<CommunityPost> findByIsTopAndStatusAndAiReviewStatus(Integer isTop, Integer status, Integer aiReviewStatus, Pageable pageable);
}
