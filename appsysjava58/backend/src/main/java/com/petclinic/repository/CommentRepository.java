package com.petclinic.repository;

import com.petclinic.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 评论数据访问接口
 * 
 * @author PetClinic Team
 * @version 1.0.0
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 根据帖子ID查询评论列表
     *
     * @param postId 帖子ID
     * @return 评论列表
     */
    List<Comment> findByPostIdAndDeletedFalseOrderByCreatedTimeDesc(Long postId);

    /**
     * 根据帖子ID统计评论数
     *
     * @param postId 帖子ID
     * @return 评论数量
     */
    long countByPostIdAndDeletedFalse(Long postId);
}