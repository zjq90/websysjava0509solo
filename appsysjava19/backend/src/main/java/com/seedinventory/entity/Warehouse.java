package com.seedinventory.entity;

import javax.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 仓库实体类
 * 用于管理多个仓库的信息
 * 
 * @author Seed Inventory Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "warehouses")
public class Warehouse {
    
    /**
     * 仓库ID，主键，自动增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 仓库编号，唯一标识
     */
    @Column(nullable = false, unique = true, length = 20)
    private String warehouseCode;
    
    /**
     * 仓库名称
     */
    @Column(nullable = false, length = 100)
    private String warehouseName;
    
    /**
     * 仓库地址
     */
    @Column(length = 255)
    private String address;
    
    /**
     * 仓库管理员
     */
    @Column(length = 50)
    private String manager;
    
    /**
     * 联系电话
     */
    @Column(length = 20)
    private String phone;
    
    /**
     * 仓库状态：ACTIVE-活跃，INACTIVE-停用
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
