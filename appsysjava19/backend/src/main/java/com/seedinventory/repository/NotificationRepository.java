package com.seedinventory.repository;

import com.seedinventory.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 消息通知数据访问层
 * 
 * @author Seed Inventory Team
 * @version 1.0.0
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    /**
     * 根据状态查询
     */
    List<Notification> findByStatusOrderByCreateTimeDesc(String status);
    
    /**
     * 根据通知类型查询
     */
    List<Notification> findByNotificationTypeOrderByCreateTimeDesc(String notificationType);
    
    /**
     * 根据批次号查询
     */
    List<Notification> findByBatchNoOrderByCreateTimeDesc(String batchNo);
    
    /**
     * 统计未读消息数量
     */
    long countByStatus(String status);
}
