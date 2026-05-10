package com.seedinventory.entity;

import javax.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 客户实体类
 * 存储客户信息，敏感数据（手机号、地址等）采用AES-256加密存储
 * 
 * @author Seed Inventory Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "customers",
       indexes = {
           @Index(name = "idx_customer_code", columnList = "customerCode"),
           @Index(name = "idx_customer_name", columnList = "customerName")
       })
public class Customer {
    
    /**
     * 客户ID，主键，自动增长
     */
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
     * 客户类型：INDIVIDUAL-个人，ENTERPRISE-企业
     */
    @Column(length = 20)
    private String customerType;
    
    /**
     * 联系人姓名
     */
    @Column(length = 50)
    private String contactName;
    
    /**
     * 手机号（加密存储），符合中国大陆手机号格式（1开头，11位）
     */
    @Column(nullable = false, length = 255)
    private String phone;
    
    /**
     * 邮箱（加密存储）
     */
    @Column(length = 255)
    private String email;
    
    /**
     * 地址（加密存储）
     */
    @Column(length = 500)
    private String address;
    
    /**
     * 状态：ACTIVE-活跃，INACTIVE-停用
     */
    @Column(nullable = false, length = 20)
    private String status = "ACTIVE";
    
    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @Column(nullable = false)
    private LocalDateTime updateTime;
    
    /**
     * 备注信息
     */
    @Column(length = 500)
    private String remark;
    
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
