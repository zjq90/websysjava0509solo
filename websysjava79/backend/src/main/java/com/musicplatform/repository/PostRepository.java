package com.musicplatform.repository;

import com.musicplatform.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByUser_Id(Long userId, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.visibility = 'PUBLIC' ORDER BY p.createdAt DESC")
    Page<Post> findAllPublicPosts(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.user.id IN :followingIds AND p.visibility <> 'PRIVATE' " +
           "ORDER BY p.createdAt DESC")
    Page<Post> findFollowingPosts(@Param("followingIds") List<Long> followingIds, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.user.id = :userId AND " +
           "(p.visibility = 'PUBLIC' OR p.visibility = 'FOLLOWERS_ONLY') ORDER BY p.createdAt DESC")
    Page<Post> findUserPublicPosts(@Param("userId") Long userId, Pageable pageable);
}
