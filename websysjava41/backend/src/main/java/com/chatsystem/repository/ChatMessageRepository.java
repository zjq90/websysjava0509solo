package com.chatsystem.repository;

import com.chatsystem.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 聊天消息数据访问接口
 */
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    /**
     * 查询两个用户之间的聊天记录（分页）
     */
    @Query("SELECT m FROM ChatMessage m WHERE " +
           "(m.fromUserId = :userId1 AND m.toUserId = :userId2) OR " +
           "(m.fromUserId = :userId2 AND m.toUserId = :userId1) " +
           "ORDER BY m.sendTime DESC")
    Page<ChatMessage> findChatHistory(Long userId1, Long userId2, Pageable pageable);

    /**
     * 查询未读消息数量
     */
    long countByFromUserIdAndToUserIdAndStatus(Long fromUserId, Long toUserId, Integer status);

    /**
     * 批量标记消息为已读
     */
    @Modifying
    @Query("UPDATE ChatMessage m SET m.status = 1 WHERE m.fromUserId = :fromUserId AND m.toUserId = :toUserId AND m.status = 0")
    int markAsRead(Long fromUserId, Long toUserId);

    /**
     * 查询与用户相关的所有消息
     */
    @Query("SELECT m FROM ChatMessage m WHERE m.fromUserId = :userId OR m.toUserId = :userId ORDER BY m.sendTime DESC")
    List<ChatMessage> findAllByUserId(Long userId);
}
