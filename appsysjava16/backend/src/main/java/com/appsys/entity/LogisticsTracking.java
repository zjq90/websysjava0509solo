package com.appsys.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 物流跟踪实体�? * 管理订单的物流信息，集成快递API，实时显示发货进�? * 
 * @author appsys-team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "logistics_tracking")
@Schema(description = "物流跟踪信息")
public class LogisticsTracking {

    /**
     * 物流记录ID，自增主�?     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "物流记录ID", example = "1")
    private Long id;

    /**
     * 关联订单ID
     */
    @Column(nullable = false)
    @Schema(description = "订单ID", example = "1")
    private Long orderId;

    /**
     * 物流公司名称
     */
    @Column(nullable = false, length = 50)
    @Schema(description = "物流公司名称", example = "顺丰速运")
    private String companyName;

    /**
     * 物流公司编码
     */
    @Column(length = 20)
    @Schema(description = "物流公司编码", example = "SF")
    private String companyCode;

    /**
     * 快递单�?     */
    @Column(nullable = false, unique = true, length = 50)
    @Schema(description = "快递单�?, example = "SF1234567890")
    private String trackingNo;

    /**
     * 当前物流状�?     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Schema(description = "物流状�?, example = "IN_TRANSIT")
    private LogisticsStatus status = LogisticsStatus.PENDING_SHIPMENT;

    /**
     * 物流跟踪详情（JSON格式存储详细轨迹�?     */
    @Column(columnDefinition = "TEXT")
    @Schema(description = "物流跟踪详情（JSON格式�?)
    private String trackingDetails;

    /**
     * 当前位置描述
     */
    @Column(length = 200)
    @Schema(description = "当前位置", example = "【北京市】快件已到达【北京转运中心�?)
    private String currentLocation;

    /**
     * 发货时间
     */
    @Schema(description = "发货时间")
    private LocalDateTime shippedAt;

    /**
     * 最后更新时�?     */
    @Schema(description = "最后更新时�?)
    private LocalDateTime lastUpdatedAt;

    /**
     * 预计送达时间
     */
    @Schema(description = "预计送达时间")
    private LocalDateTime estimatedDeliveryAt;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    @Schema(description = "创建时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        lastUpdatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdatedAt = LocalDateTime.now();
    }
}
