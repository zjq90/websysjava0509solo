package com.musicplatform.repository;

import com.musicplatform.entity.Follow;
import com.musicplatform.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollower_IdAndFollowing_Id(Long followerId, Long followingId);

    boolean existsByFollower_IdAndFollowing_Id(Long followerId, Long followingId);

    @Query("SELECT f.following FROM Follow f WHERE f.follower.id = :followerId")
    Page<User> findFollowingByFollowerId(@Param("followerId") Long followerId, Pageable pageable);

    @Query("SELECT f.follower FROM Follow f WHERE f.following.id = :followingId")
    Page<User> findFollowersByFollowingId(@Param("followingId") Long followingId, Pageable pageable);

    void deleteByFollower_IdAndFollowing_Id(Long followerId, Long followingId);
}
