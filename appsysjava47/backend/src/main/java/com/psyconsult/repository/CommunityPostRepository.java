package com.psyconsult.repository;

import com.psyconsult.entity.CommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {
    List<CommunityPost> findByUserIdOrderByCreateTimeDesc(Long userId);
    List<CommunityPost> findByStatusOrderByCreateTimeDesc(String status);
}
