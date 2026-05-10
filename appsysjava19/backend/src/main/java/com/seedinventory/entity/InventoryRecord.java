package com.seedinventory.entity;

import javax.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 出入库记录实体类
 * 记录所有入库和出库操作的历史记录
 * 
 * @author Seed Inventory Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "inventory_records",
       indexes = {
           @Index(name = "idx_batch_no_record", columnList = "batchNo"),
           @Index(name = "idx_record_type", columnList = "recordType"),
           @Index(name = "idx_record_time", columnList = "recordTime")
       })
public class InventoryRecord {
    
    /**
     * 记录ID，主键，自动增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 记录编号
     */
    @Column(nullable = false, unique = true, length = 30)
    private String recordNo;
    
    /**
     * 记录类型：INBOUND-入库，OUTBOUND-出库
     */
    @Column(nullable = false, length = 20)
    private String recordType;
    
    /**
     * 批次编号
     */
    @Column(nullable = false, length = 8)
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
     * 操作数量
     */
    @Column(nullable = false, precision = 15, scale = 3)
    private BigDecimal quantity;
    
    /**
     * 单位
     */
    @Column(length = 10)
    private String unit;
    
    /**
     * 操作前数量
     */
    @Column(precision = 15, scale = 3)
    private BigDecimal beforeQuantity;
    
    /**
     * 操作后数量
     */
    @Column(precision = 15, scale = 3)
    private BigDecimal afterQuantity;
    
    /**
     * 关联单号（如采购单号、销售单号）
     */
    @Column(length = 30)
    private String relatedNo;
    
    /**
     * 客户ID（出库时关联）
     */
    @Column
    private Long customerId;
    
    /**
     * 操作人员
     */
    @Column(length = 50)
    private String operator;
    
    /**
     * 记录时间
     */
    @Column(nullable = false)
    private LocalDateTime recordTime;
    
    /**
     * 状态：COMPLETED-已完成，CANCELLED-已取消
     */
    @Column(nullable = false, length = 20)
    private String status = "COMPLETED";
    
    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;
    
    /**
     * 备注信息
     */
    @Column(length = 500)
    private String remark;
    
    @PrePersist
    protected void onCreate() {
        if (recordTime == null) {
            recordTime = LocalDateTime.now();
        }
        createTime = LocalDateTime.now();
    }
}
