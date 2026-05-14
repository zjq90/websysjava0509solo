package com.appsys.repository;

import com.appsys.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdOrUserIdIsNullOrderByCreateTimeDesc(Long userId);

    List<Notification> findByUserIdAndTypeOrderByCreateTimeDesc(Long userId, Integer type);

    long countByUserIdAndIsRead(Long userId, Boolean isRead);

    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.id = :id")
    int markAsRead(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.userId = :userId OR n.userId IS NULL")
    int markAllAsRead(Long userId);
}
