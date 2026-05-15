package com.chatsystem.repository;

import com.chatsystem.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 好友关系数据访问接口
 */
@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    /**
     * 查询用户的好友列表（已添加）
     */
    List<Friend> findByUserIdAndStatus(Long userId, Integer status);

    /**
     * 查询好友请求列表
     */
    List<Friend> findByFriendIdAndStatus(Long friendId, Integer status);

    /**
     * 查询好友关系
     */
    Optional<Friend> findByUserIdAndFriendId(Long userId, Long friendId);

    /**
     * 查询是否已存在好友关系
     */
    boolean existsByUserIdAndFriendId(Long userId, Long friendId);

    /**
     * 查询用户的好友ID列表
     */
    @Query("SELECT f.friendId FROM Friend f WHERE f.userId = :userId AND f.status = 1")
    List<Long> findFriendIdsByUserId(Long userId);
}
