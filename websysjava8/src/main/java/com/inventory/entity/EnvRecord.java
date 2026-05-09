package com.inventory.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 环境记录实体类
 * 用于记录仓库的温湿度数据
 * 支持温湿度预警功能
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "env_record")
public class EnvRecord extends BaseEntity {

    /**
     * 仓库ID
     */
    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    /**
     * 仓库信息（多对一关系）
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", insertable = false, updatable = false)
    private Warehouse warehouse;

    /**
     * 记录时间
     */
    @Column(name = "record_time", nullable = false)
    private LocalDateTime recordTime;

    /**
     * 温度（摄氏度）
     */
    @Column(name = "temperature", nullable = false)
    private Double temperature;

    /**
     * 湿度（百分比）
     */
    @Column(name = "humidity", nullable = false)
    private Double humidity;

    /**
     * 温度预警阈值最小值（记录时的阈值）
     */
    @Column(name = "temp_threshold_min")
    private Double tempThresholdMin;

    /**
     * 温度预警阈值最大值（记录时的阈值）
     */
    @Column(name = "temp_threshold_max")
    private Double tempThresholdMax;

    /**
     * 湿度预警阈值最小值（记录时的阈值）
     */
    @Column(name = "humidity_threshold_min")
    private Double humidityThresholdMin;

    /**
     * 湿度预警阈值最大值（记录时的阈值）
     */
    @Column(name = "humidity_threshold_max")
    private Double humidityThresholdMax;

    /**
     * 状态：0-正常，1-温度异常，2-湿度异常，3-温湿度都异常
     */
    @Column(name = "status", nullable = false)
    private Integer status = 0;

    /**
     * 预警信息
     */
    @Column(name = "warning_message", length = 200)
    private String warningMessage;

    /**
     * 记录来源：1-自动采集，2-人工录入
     */
    @Column(name = "source_type", nullable = false)
    private Integer sourceType = 1;

    /**
     * 备注
     */
    @Column(name = "remark", length = 200)
    private String remark;
}
