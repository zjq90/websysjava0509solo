package com.inventory.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import java.time.LocalDate;

/**
 * 库存实体类
 * 核心表，管理具体的库存信息
 * 关联仓库/门店和种子批次
 * 支持先进先出（FIFO）库存管理策略
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "inventory")
public class Inventory extends BaseEntity {

    /**
     * 库存类型：1-仓库库存，2-门店库存
     */
    @Column(name = "inventory_type", nullable = false)
    private Integer inventoryType;

    /**
     * 位置ID（仓库ID或门店ID）
     */
    @Column(name = "location_id", nullable = false)
    private Long locationId;

    /**
     * 批次ID
     */
    @Column(name = "batch_id", nullable = false)
    private Long batchId;

    /**
     * 批次信息（多对一关系）
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", insertable = false, updatable = false)
    private SeedBatch seedBatch;

    /**
     * 当前库存数量
     */
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /**
     * 锁定数量（如已下单待出库）
     */
    @Column(name = "locked_quantity", nullable = false)
    private Integer lockedQuantity = 0;

    /**
     * 可用数量 = 库存数量 - 锁定数量
     */
    @Column(name = "available_quantity", nullable = false)
    private Integer availableQuantity;

    /**
     * 入库日期（用于先进先出排序）
     */
    @Column(name = "inbound_date", nullable = false)
    private LocalDate inboundDate;

    /**
     * 库存状态：
     * 0-正常
     * 1-预警（库存不足）
     * 2-预警（近效期）
     * 3-已过期
     */
    @Column(name = "status", nullable = false)
    private Integer status = 0;

    /**
     * 最低库存预警阈值
     */
    @Column(name = "min_stock_warning")
    private Integer minStockWarning;

    /**
     * 最高库存预警阈值
     */
    @Column(name = "max_stock_warning")
    private Integer maxStockWarning;

    /**
     * 入库单号
     */
    @Column(name = "inbound_no", length = 50)
    private String inboundNo;

    /**
     * 备注
     */
    @Column(name = "remark", length = 500)
    private String remark;
}
