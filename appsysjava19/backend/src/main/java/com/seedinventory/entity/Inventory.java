package com.seedinventory.entity;

import javax.persistence.*;
import lombok.Data;



import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 库存实体类
 * 存储种子库存信息，包含批次号、保质期、发芽率等关键信息
 * 
 * @author Seed Inventory Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "inventory",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "batchNo")
       },
       indexes = {
           @Index(name = "idx_warehouse_seed", columnList = "warehouseId, seedId"),
           @Index(name = "idx_batch_no", columnList = "batchNo"),
           @Index(name = "idx_expiry_date", columnList = "expiryDate")
       })
public class Inventory {
    
    /**
     * 库存ID，主键，自动增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 批次编号，必须为8位数字+字母组合，且全局唯一
     */
    @Column(nullable = false, unique = true, length = 8)
    private String batchNo;
    
    /**
     * 仓库ID
     */
    @Column(nullable = false)
    private Long warehouseId;
    
    /**
     * 种子ID
     */
    @Column(nullable = false)
    private Long seedId;
    
    /**
     * 当前库存量
     */
    @Column(nullable = false, precision = 15, scale = 3)
    private BigDecimal quantity;
    
    /**
     * 单位
     */
    @Column(length = 10)
    private String unit;
    
    /**
     * 保质期，不得早于当前日期+6个月
     */
    @Column(nullable = false)
    private LocalDate expiryDate;
    
    /**
     * 发芽率，数值范围：0–100%，精度保留1位小数
     */
    @Column(nullable = false, precision = 4, scale = 1)
    private BigDecimal germinationRate;
    
    /**
     * 入库时间
     */
    @Column(nullable = false)
    private LocalDateTime inboundTime;
    
    /**
     * 产地
     */
    @Column(length = 100)
    private String origin;
    
    /**
     * 存储位置
     */
    @Column(length = 50)
    private String storageLocation;
    
    /**
     * 库存状态：NORMAL-正常，NEAR_EXPIRY-近效期，EXPIRED-已过期，LOW_STOCK-缺货
     */
    @Column(nullable = false, length = 20)
    private String status = "NORMAL";
    
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
        if (inboundTime == null) {
            inboundTime = LocalDateTime.now();
        }
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
