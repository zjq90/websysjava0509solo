package com.agriculture.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 客户实体类
 * 存储客户信息，敏感字段（联系方式）采用AES-256加密存储
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 客户编号
     */
    @Column(nullable = false, unique = true, length = 30)
    private String customerCode;

    /**
     * 客户名称
     */
    @Column(nullable = false, length = 100)
    private String customerName;

    /**
     * 客户类型：COMPANY-企业，INDIVIDUAL-个人，RESEARCH-科研单位
     */
    @Column(length = 20)
    private String customerType;

    /**
     * 联系人（加密存储）
     */
    @Column(length = 255)
    private String contactPerson;

    /**
     * 客户手机号（加密存储）：符合中国大陆手机号格式（1开头，11位）
     */
    @Column(length = 255)
    private String phone;

    /**
     * 客户邮箱（加密存储）
     */
    @Column(length = 255)
    private String email;

    /**
     * 地址
     */
    @Column(length = 500)
    private String address;

    /**
     * 所属地区
     */
    @Column(length = 100)
    private String region;

    /**
     * 客户等级：VIP-重要客户，NORMAL-普通客户
     */
    @Column(length = 20)
    private String customerLevel;

    /**
     * 备注
     */
    @Column(columnDefinition = "TEXT")
    private String remarks;

    /**
     * 状态：ACTIVE-正常，INACTIVE-停用
     */
    @Column(nullable = false, length = 20)
    private String status;

    /**
     * 创建人ID
     */
    private Long createdBy;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = "ACTIVE";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
