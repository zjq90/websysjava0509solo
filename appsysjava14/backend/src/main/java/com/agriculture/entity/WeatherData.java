package com.agriculture.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 气象数据实体类
 * 对接气象API，实时获取并记录种植区域环境数据
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "weather_data")
public class WeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联地块ID
     */
    private Long plotId;

    /**
     * 城市编码
     */
    @Column(length = 20)
    private String cityCode;

    /**
     * 城市名称
     */
    @Column(length = 50)
    private String cityName;

    /**
     * 数据日期
     */
    @Column(nullable = false)
    private LocalDateTime recordDate;

    /**
     * 天气状况：SUNNY-晴，CLOUDY-阴，RAINY-雨，PARTLY_CLOUDY-多云等
     */
    @Column(length = 20)
    private String weatherCondition;

    /**
     * 温度（℃）
     */
    @Column(precision = 5, scale = 1)
    private BigDecimal temperature;

    /**
     * 体感温度
     */
    @Column(precision = 5, scale = 1)
    private BigDecimal feelsLike;

    /**
     * 最低温度（℃）
     */
    @Column(precision = 5, scale = 1)
    private BigDecimal tempMin;

    /**
     * 最高温度（℃）
     */
    @Column(precision = 5, scale = 1)
    private BigDecimal tempMax;

    /**
     * 湿度（%）
     */
    @Column(precision = 5, scale = 1)
    private BigDecimal humidity;

    /**
     * 风向
     */
    @Column(length = 20)
    private String windDirection;

    /**
     * 风速（m/s）
     */
    @Column(precision = 5, scale = 1)
    private BigDecimal windSpeed;

    /**
     * 云量（%）
     */
    @Column(precision = 5, scale = 1)
    private BigDecimal cloudCover;

    /**
     * 降水概率（%）
     */
    @Column(precision = 6, scale = 1)
    private BigDecimal precipitation;

    /**
     * 紫外线强度
     */
    @Column(length = 20)
    private String uvIndex;

    /**
     * 空气质量指数
     */
    @Column
    private Integer aqi;

    /**
     * 数据来源：API-气象API，MANUAL-手动录入
     */
    @Column(length = 20)
    private String dataSource;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdTime;

    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
        if (recordDate == null) {
            recordDate = LocalDateTime.now();
        }
        if (dataSource == null) {
            dataSource = "API";
        }
    }
}
