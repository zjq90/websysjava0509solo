package com.agriculture.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 田间记录实体类
 * 存储田间数据采集的主记录信息
 * 
 * 采集流程：
 * 用户登录 -> 选择"田间记录"模块 -> 定位当前地块 -> 选择作物品种 -> 
 * 录入生长阶段 -> 填写观测数据 -> 上传照片 -> 提交
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "field_records")
public class FieldRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 记录编号，唯一标识
     */
    @Column(nullable = false, unique = true, length = 30)
    private String recordNo;

    /**
     * 关联地块ID
     */
    @Column(nullable = false)
    private Long plotId;

    /**
     * 关联地块名称（冗余存储，便于查询）
     */
    @Column(length = 100)
    private String plotName;

    /**
     * 关联作物品种ID
     */
    @Column(nullable = false)
    private Long cropId;

    /**
     * 关联作物品种名称（冗余存储）
     */
    @Column(length = 100)
    private String cropName;

    /**
     * 生长阶段：
     * SEEDLING-出苗期，TILLERING-分蘖期，JOINTING-拔节期，
     * BOOTING-孕穗期，HEADING-抽穗期，FLOWERING-开花期，
     * FILLING-灌浆期，MATURING-成熟期，HARVEST-收获期
     */
    @Column(nullable = false, length = 20)
    private String growthStage;

    /**
     * 记录日期
     */
    @Column(nullable = false)
    private LocalDateTime recordDate;

    /**
     * 株高（cm）
     */
    @Column(precision = 6, scale = 1)
    private BigDecimal plantHeight;

    /**
     * 出苗率（%）
     */
    @Column(precision = 5, scale = 1)
    private BigDecimal emergenceRate;

    /**
     * 病虫害等级：0-无，1-轻度，2-中度，3-重度，4-严重
     */
    @Column
    private Integer pestLevel;

    /**
     * 病虫害种类
     */
    @Column(length = 200)
    private String pestType;

    /**
     * 叶色：DARK_GREEN-深绿，GREEN-绿色，LIGHT_GREEN-浅绿，YELLOW-黄色
     */
    @Column(length = 20)
    private String leafColor;

    /**
     * 生育期描述
     */
    @Column(columnDefinition = "TEXT")
    private String growthDescription;

    /**
     * 气候状况：SUNNY-晴，CLOUDY-阴，RAINY-雨，WINDY-风
     */
    @Column(length = 20)
    private String weatherCondition;

    /**
     * 温度（℃）
     */
    @Column(precision = 5, scale = 1)
    private BigDecimal temperature;

    /**
     * 湿度（%）
     */
    @Column(precision = 5, scale = 1)
    private BigDecimal humidity;

    /**
     * 观测人
     */
    @Column(nullable = false, length = 50)
    private String observer;

    /**
     * 观测人ID
     */
    private Long observerId;

    /**
     * 数据来源：ONLINE-在线录入，OFFLINE-离线同步
     */
    @Column(length = 20)
    private String dataSource;

    /**
     * 记录状态：DRAFT-草稿，SUBMITTED-已提交，REVIEWED-已审核
     */
    @Column(nullable = false, length = 20)
    private String status;

    /**
     * 备注
     */
    @Column(columnDefinition = "TEXT")
    private String remarks;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (recordDate == null) {
            recordDate = LocalDateTime.now();
        }
        if (status == null) {
            status = "SUBMITTED";
        }
        if (dataSource == null) {
            dataSource = "ONLINE";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedTime() {
        return this.createdAt;
    }

    public LocalDateTime getUpdatedTime() {
        return this.updatedAt;
    }
}
