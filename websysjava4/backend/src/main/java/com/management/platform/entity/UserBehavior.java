package com.management.platform.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户行为记录实体类
 * 存储用户的各种行为数据，用于用户行为分析
 */
@Entity
@Table(name = "user_behaviors")
public class UserBehavior {

    /**
     * 行为记录ID，主键自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户ID
     */
    @Column(nullable = false)
    private Long userId;

    /**
     * 行为类型：login（登录）, browse（浏览）, add_to_cart（加购）, purchase（购买）, logout（登出）
     */
    @Column(nullable = false, length = 50)
    private String behaviorType;

    /**
     * 关联商品ID（可选）
     */
    private Long productId;

    /**
     * 行为描述
     */
    @Column(length = 500)
    private String description;

    /**
     * 行为发生时间
     */
    @Column(nullable = false)
    private LocalDateTime behaviorTime;

    /**
     * 创建时间
     */
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (behaviorTime == null) {
            behaviorTime = LocalDateTime.now();
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getBehaviorType() { return behaviorType; }
    public void setBehaviorType(String behaviorType) { this.behaviorType = behaviorType; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getBehaviorTime() { return behaviorTime; }
    public void setBehaviorTime(LocalDateTime behaviorTime) { this.behaviorTime = behaviorTime; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
