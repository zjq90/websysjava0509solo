package com.seedinventory.entity;

import javax.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 消息通知实体类
 * 存储近效期预警、缺货提醒等消息
 * 
 * @author Seed Inventory Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "notifications",
       indexes = {
           @Index(name = "idx_notification_type", columnList = "notificationType"),
           @Index(name = "idx_notification_status", columnList = "status"),
           @Index(name = "idx_create_time_notif", columnList = "createTime")
       })
public class Notification {
    
    /**
     * 通知ID，主键，自动增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 通知类型：NEAR_EXPIRY-近效期预警，LOW_STOCK-缺货提醒，SYSTEM-系统通知
     */
    @Column(nullable = false, length = 30)
    private String notificationType;
    
    /**
     * 通知标题
     */
    @Column(nullable = false, length = 100)
    private String title;
    
    /**
     * 通知内容
     */
    @Column(nullable = false, length = 1000)
    private String content;
    
    /**
     * 关联批次号
     */
    @Column(length = 8)
    private String batchNo;
    
    /**
     * 关联仓库ID
     */
    @Column
    private Long warehouseId;
    
    /**
     * 关联种子ID
     */
    @Column
    private Long seedId;
    
    /**
     * 状态：UNREAD-未读，READ-已读，HANDLED-已处理
     */
    @Column(nullable = false, length = 20)
    private String status = "UNREAD";
    
    /**
     * 优先级：LOW-低，MEDIUM-中，HIGH-高
     */
    @Column(length = 20)
    private String priority = "MEDIUM";
    
    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;
    
    /**
     * 阅读时间
     */
    @Column
    private LocalDateTime readTime;
    
    /**
     * 处理人
     */
    @Column(length = 50)
    private String handler;
    
    /**
     * 处理时间
     */
    @Column
    private LocalDateTime handleTime;
    
    /**
     * 处理备注
     */
    @Column(length = 500)
    private String handleRemark;
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
