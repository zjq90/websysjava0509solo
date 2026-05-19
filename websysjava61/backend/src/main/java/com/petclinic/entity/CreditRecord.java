package com.petclinic.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 信用分记录实体类
 */
@Data
@Entity
@Table(name = "credit_record")
public class CreditRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户ID
     */
    @Column(nullable = false)
    private Long userId;

    /**
     * 用户类型：1-宠物主人，2-医生
     */
    @Column(nullable = false)
    private Integer userType;

    /**
     * 分数变化（正数加分，负数扣分）
     */
    @Column(nullable = false)
    private Integer scoreChange;

    /**
     * 变更后分数
     */
    @Column(nullable = false)
    private Integer currentScore;

    /**
     * 变更原因
     */
    @Column(nullable = false, length = 500)
    private String reason;

    /**
     * 关联业务ID
     */
    private Long businessId;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
