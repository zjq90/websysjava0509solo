package com.club.management.message.repository;

import com.club.management.message.entity.MessageReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 消息已读状态Repository
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface MessageReadStatusRepository extends JpaRepository<MessageReadStatus, Long>, JpaSpecificationExecutor<MessageReadStatus> {

    List<MessageReadStatus> findByMessageIdAndMessageType(Long messageId, Integer messageType);

    MessageReadStatus findByMessageIdAndMessageTypeAndUserId(Long messageId, Integer messageType, Long userId);

    List<MessageReadStatus> findByMessageIdAndMessageTypeAndIsRead(Long messageId, Integer messageType, Integer isRead);

    @Modifying
    @Query("UPDATE MessageReadStatus s SET s.isReminded = 1, s.remindCount = s.remindCount + 1 " +
           "WHERE s.messageId = :messageId AND s.messageType = :messageType AND s.userId = :userId")
    int updateRemindStatus(@Param("messageId") Long messageId, @Param("messageType") Integer messageType, @Param("userId") Long userId);

    long countByMessageIdAndMessageTypeAndIsRead(Long messageId, Integer messageType, Integer isRead);
}
