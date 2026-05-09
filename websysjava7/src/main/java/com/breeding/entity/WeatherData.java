package com.breeding.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * 气象数据实体类
 * 用于记录田间试验地点的气象数据，包括温度、湿度、降雨、日照等
 * 用于分析环境因素对育种的影响
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "weather_data")
public class WeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 数据记录日期
     */
    @Column(nullable = false)
    private LocalDate recordDate;

    /**
     * 试验地点
     */
    @Column(nullable = false, length = 200)
    private String location;

    /**
     * 日平均气温（℃）
     */
    private Double avgTemperature;

    /**
     * 日最高气温（℃）
     */
    private Double maxTemperature;

    /**
     * 日最低气温（℃）
     */
    private Double minTemperature;

    /**
     * 平均相对湿度（%）
     */
    private Double avgHumidity;

    /**
     * 日降雨量（毫米）
     */
    private Double rainfall;

    /**
     * 日照时数（小时）
     */
    private Double sunshineHours;

    /**
     * 平均风速（米/秒）
     */
    private Double avgWindSpeed;

    /**
     * 最大风速（米/秒）
     */
    private Double maxWindSpeed;

    /**
     * 风向
     */
    @Column(length = 20)
    private String windDirection;

    /**
     * 气压（百帕）
     */
    private Double airPressure;

    /**
     * 蒸发量（毫米）
     */
    private Double evaporation;

    /**
     * 天气现象描述
     */
    @Column(length = 100)
    private String weatherDescription;

    /**
     * 备注
     */
    @Column(length = 500)
    private String remarks;

    /**
     * 关联的田间试验
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experiment_id")
    private FieldExperiment experiment;

    /**
     * 数据来源
     */
    @Column(length = 50)
    private String dataSource;

    /**
     * 创建时间
     */
    @Column(nullable = false)
    private LocalDate createDate;

    /**
     * 更新时间
     */
    private LocalDate updateDate;

    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateDate = LocalDate.now();
    }
}
