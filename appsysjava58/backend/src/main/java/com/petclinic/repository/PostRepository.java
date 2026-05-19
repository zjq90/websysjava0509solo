package com.petclinic.repository;

import com.petclinic.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 帖子数据访问接口
 * 
 * @author PetClinic Team
 * @version 1.0.0
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByStatusAndDeletedFalse(String status, Pageable pageable);

    Page<Post> findByUserIdAndDeletedFalse(Long userId, Pageable pageable);

    List<Post> findByUserIdAndStatusAndDeletedFalse(Long userId, String status);

    List<Post> findByTagsContainingAndStatusAndDeletedFalse(String tag, String status);
}