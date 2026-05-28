package com.club.management.message.repository;

import com.club.management.message.entity.PrivateMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 私聊消息Repository
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, Long>, JpaSpecificationExecutor<PrivateMessage> {

    @Query("SELECT m FROM PrivateMessage m WHERE " +
           "(m.senderId = :userId1 AND m.receiverId = :userId2) OR " +
           "(m.senderId = :userId2 AND m.receiverId = :userId1) " +
           "ORDER BY m.createTime DESC")
    Page<PrivateMessage> findConversationMessages(@Param("userId1") Long userId1, @Param("userId2") Long userId2, Pageable pageable);

    List<PrivateMessage> findByReceiverIdAndIsRead(Long receiverId, Integer isRead);

    @Modifying
    @Query("UPDATE PrivateMessage m SET m.isRead = 1 WHERE m.senderId = :senderId AND m.receiverId = :receiverId AND m.isRead = 0")
    int markConversationAsRead(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);

    long countByReceiverIdAndIsRead(Long receiverId, Integer isRead);
}
