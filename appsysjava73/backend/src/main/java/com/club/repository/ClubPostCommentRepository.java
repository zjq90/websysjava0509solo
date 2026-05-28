package com.club.repository;

import com.club.entity.ClubPostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 帖子评论Repository接口
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface ClubPostCommentRepository extends JpaRepository<ClubPostComment, Long>, JpaSpecificationExecutor<ClubPostComment> {

    Page<ClubPostComment> findByPostIdAndParentIdAndStatusAndDeletedFalseOrderByCreateTimeDesc(Long postId, Long parentId, Integer status, Pageable pageable);

    List<ClubPostComment> findByPostIdAndParentIdAndStatusAndDeletedFalseOrderByCreateTimeDesc(Long postId, Long parentId, Integer status);

    List<ClubPostComment> findByParentIdAndStatusAndDeletedFalse(Long parentId, Integer status);

    Optional<ClubPostComment> findByIdAndDeletedFalse(Long id);
}
