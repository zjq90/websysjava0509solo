package com.heritage.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 环境监测数据实体类
 * 
 * @author Heritage Team
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "environment_data")
public class EnvironmentData extends BaseEntity {

    /**
     * 关联收藏ID
     */
    @Column(name = "collection_item_id", nullable = false)
    private Long collectionItemId;

    /**
     * 设备ID
     */
    @Column(name = "device_id", nullable = false, length = 100)
    private String deviceId;

    /**
     * 记录时间
     */
    @Column(name = "record_time", nullable = false)
    private LocalDateTime recordTime;

    /**
     * 温度（摄氏度）
     */
    @Column(name = "temperature", precision = 5, scale = 2)
    private java.math.BigDecimal temperature;

    /**
     * 湿度（百分比）
     */
    @Column(name = "humidity", precision = 5, scale = 2)
    private java.math.BigDecimal humidity;

    /**
     * 光照强度（勒克斯）
     */
    @Column(name = "light_intensity", precision = 10, scale = 2)
    private java.math.BigDecimal lightIntensity;

    /**
     * 是否异常：0-正常，1-异常
     */
    @Column(name = "is_abnormal", nullable = false)
    private Integer isAbnormal = 0;

    /**
     * 异常类型：1-温度异常，2-湿度异常，3-光照异常
     */
    @Column(name = "abnormal_type")
    private Integer abnormalType;

    /**
     * 异常说明
     */
    @Column(name = "abnormal_desc", length = 500)
    private String abnormalDesc;

    /**
     * 建议措施
     */
    @Column(name = "suggestion", length = 500)
    private String suggestion;
}