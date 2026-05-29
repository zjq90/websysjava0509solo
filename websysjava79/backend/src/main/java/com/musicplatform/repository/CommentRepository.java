package com.musicplatform.repository;

import com.musicplatform.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByTargetTypeAndTargetIdAndParentIsNull(
            Comment.TargetType targetType, Long targetId, Pageable pageable);

    List<Comment> findByParent_Id(Long parentId);

    long countByTargetTypeAndTargetId(Comment.TargetType targetType, Long targetId);

    List<Comment> findByUser_Id(Long userId);
}
