package com.inventory.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 盘点单实体类
 * 用于管理库存盘点操作
 * 支持定期盘点和临时盘点
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "stock_take")
public class StockTake extends BaseEntity {

    /**
     * 盘点单号，唯一标识
     */
    @Column(name = "order_no", nullable = false, unique = true, length = 50)
    private String orderNo;

    /**
     * 盘点类型：
     * 1-定期盘点
     * 2-临时盘点
     */
    @Column(name = "take_type", nullable = false)
    private Integer takeType = 1;

    /**
     * 库存位置ID（仓库ID或门店ID）
     */
    @Column(name = "location_id", nullable = false)
    private Long locationId;

    /**
     * 库存位置类型：1-仓库，2-门店
     */
    @Column(name = "location_type", nullable = false)
    private Integer locationType;

    /**
     * 状态：
     * 0-新建
     * 1-盘点中
     * 2-已完成
     * 3-已取消
     */
    @Column(name = "status", nullable = false)
    private Integer status = 0;

    /**
     * 盘点开始时间
     */
    @Column(name = "start_time")
    private LocalDateTime startTime;

    /**
     * 盘点完成时间
     */
    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    /**
     * 盘点人
     */
    @Column(name = "taker", length = 50)
    private String taker;

    /**
     * 复盘人
     */
    @Column(name = "rechecker", length = 50)
    private String rechecker;

    /**
     * 差异数量总数
     */
    @Column(name = "diff_quantity_total")
    private Integer diffQuantityTotal = 0;

    /**
     * 差异金额总数
     */
    @Column(name = "diff_amount_total")
    private Double diffAmountTotal = 0.0;

    /**
     * 盘点说明
     */
    @Column(name = "description", length = 500)
    private String description;

    /**
     * 备注
     */
    @Column(name = "remark", length = 500)
    private String remark;
}
