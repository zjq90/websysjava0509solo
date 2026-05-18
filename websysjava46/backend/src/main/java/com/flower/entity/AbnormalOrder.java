package com.flower.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 异常订单实体类
 * 对应数据库表abnormal_order
 */
@Data
@Entity
@Table(name = "abnormal_order")
public class AbnormalOrder {

    /**
     * 异常记录ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 订单ID
     */
    @Column(nullable = false)
    private Long orderId;

    /**
     * 订单编号
     */
    @Column(nullable = false, length = 50)
    private String orderNo;

    /**
     * 异常类型：1-退款，2-补发，3-赔偿，4-其他
     */
    @Column(nullable = false)
    private Integer type;

    /**
     * 异常原因
     */
    @Column(nullable = false, length = 500)
    private String reason;

    /**
     * 处理状态：0-待处理，1-处理中，2-已完成
     */
    @Column(nullable = false)
    private Integer status = 0;

    /**
     * 处理结果
     */
    @Column(length = 500)
    private String result;

    /**
     * 退款/赔偿金额
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    /**
     * 处理人
     */
    @Column(length = 50)
    private String handler;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 处理完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime handleTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
