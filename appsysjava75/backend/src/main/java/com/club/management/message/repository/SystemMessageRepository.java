package com.club.management.message.repository;

import com.club.management.message.entity.SystemMessage;
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
 * 系统消息Repository
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface SystemMessageRepository extends JpaRepository<SystemMessage, Long>, JpaSpecificationExecutor<SystemMessage> {

    Page<SystemMessage> findByReceiverIdOrReceiverIdIsNullOrderByCreateTimeDesc(Long receiverId, Pageable pageable);

    Page<SystemMessage> findByClubIdOrderByCreateTimeDesc(Long clubId, Pageable pageable);

    List<SystemMessage> findByReceiverIdAndIsRead(Long receiverId, Integer isRead);

    @Modifying
    @Query("UPDATE SystemMessage m SET m.isRead = 1 WHERE m.receiverId = :receiverId AND m.isRead = 0")
    int markAllAsRead(@Param("receiverId") Long receiverId);

    long countByReceiverIdAndIsRead(Long receiverId, Integer isRead);
}
