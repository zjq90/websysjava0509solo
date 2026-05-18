package com.flower.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 客户实体类
 * 对应数据库表customer
 */
@Data
@Entity
@Table(name = "customer")
public class Customer {

    /**
     * 客户ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 客户姓名
     */
    @Column(nullable = false, length = 50)
    private String name;

    /**
     * 手机号
     */
    @Column(nullable = false, unique = true, length = 20)
    private String phone;

    /**
     * 邮箱
     */
    @Column(length = 100)
    private String email;

    /**
     * 性别：0-未知，1-男，2-女
     */
    private Integer gender = 0;

    /**
     * 头像URL
     */
    @Column(length = 500)
    private String avatar;

    /**
     * 累计消费金额
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal totalConsumption = BigDecimal.ZERO;

    /**
     * 订单数量
     */
    private Integer orderCount = 0;

    /**
     * 客户标签（多个标签用逗号分隔）
     */
    @Column(length = 200)
    private String tags;

    /**
     * 客户等级：1-普通，2-银卡，3-金卡，4-钻石
     */
    private Integer level = 1;

    /**
     * 状态：0-禁用，1-启用
     */
    @Column(nullable = false)
    private Integer status = 1;

    /**
     * 备注
     */
    @Column(length = 500)
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
