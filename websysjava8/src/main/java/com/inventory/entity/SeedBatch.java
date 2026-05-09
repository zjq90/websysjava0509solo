package com.inventory.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import java.time.LocalDate;

/**
 * 种子批次实体类
 * 用于管理种子的批次信息
 * 每个批次对应一个具体的品种（Variety）
 * 包含批次号、生产日期、保质期等关键信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "seed_batch")
public class SeedBatch extends BaseEntity {

    /**
     * 批次编号，唯一标识
     * 格式建议：品种编码 + 日期 + 序号
     */
    @Column(name = "batch_no", nullable = false, unique = true, length = 100)
    private String batchNo;

    /**
     * 所属品种ID
     */
    @Column(name = "variety_id", nullable = false)
    private Long varietyId;

    /**
     * 所属品种（多对一关系）
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variety_id", insertable = false, updatable = false)
    private Variety variety;

    /**
     * 生产日期
     */
    @Column(name = "production_date", nullable = false)
    private LocalDate productionDate;

    /**
     * 保质期至（过期日期）
     */
    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    /**
     * 总数量（入库数量）
     */
    @Column(name = "total_quantity", nullable = false)
    private Integer totalQuantity;

    /**
     * 剩余可用数量
     */
    @Column(name = "available_quantity", nullable = false)
    private Integer availableQuantity;

    /**
     * 单位（如：袋、公斤、粒等）
     */
    @Column(name = "unit", length = 20)
    private String unit;

    /**
     * 采购单价
     */
    @Column(name = "unit_price")
    private Double unitPrice;

    /**
     * 供应商
     */
    @Column(name = "supplier", length = 100)
    private String supplier;

    /**
     * 批次状态
     * 0-正常，1-预警（近效期），2-已过期
     */
    @Column(name = "status", nullable = false)
    private Integer status = 0;

    /**
     * 备注信息
     */
    @Column(name = "remark", length = 500)
    private String remark;
}
