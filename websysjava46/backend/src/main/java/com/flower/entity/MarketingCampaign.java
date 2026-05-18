package com.flower.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 营销活动实体类
 * 对应数据库表marketing_campaign
 */
@Data
@Entity
@Table(name = "marketing_campaign")
public class MarketingCampaign {

    /**
     * 活动ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 活动名称
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * 活动类型：1-优惠券，2-折扣，3-满减，4-拼团
     */
    @Column(nullable = false)
    private Integer type;

    /**
     * 目标客户标签（多个用逗号分隔）
     */
    @Column(length = 200)
    private String targetTags;

    /**
     * 优惠金额/折扣率
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal discountAmount;

    /**
     * 使用门槛
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal threshold;

    /**
     * 活动开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime startTime;

    /**
     * 活动结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime endTime;

    /**
     * 状态：0-未开始，1-进行中，2-已结束，3-已取消
     */
    @Column(nullable = false)
    private Integer status = 0;

    /**
     * 发放数量
     */
    private Integer totalCount = 0;

    /**
     * 使用数量
     */
    private Integer usedCount = 0;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
