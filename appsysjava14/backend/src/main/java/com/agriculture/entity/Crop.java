package com.agriculture.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 作物品种实体类
 * 存储育种试验的作物品种信息
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "crops")
public class Crop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 批次编号：必须为8位数字+字母组合，且全局唯一
     */
    @Column(nullable = false, unique = true, length = 8)
    private String batchCode;

    /**
     * 作物类型：MAIZE-玉米，WHEAT-小麦，RICE-水稻，SOYBEAN-大豆等
     */
    @Column(nullable = false, length = 20)
    private String cropType;

    /**
     * 作物名称（品种名称）
     */
    @Column(nullable = false, length = 100)
    private String cropName;

    /**
     * 品种编号
     */
    @Column(length = 50)
    private String varietyCode;

    /**
     * 培育单位（生产商）
     */
    @Column(length = 200)
    private String producer;

    /**
     * 培育人
     */
    @Column(length = 50)
    private String breeder;

    /**
     * 发芽率：数值范围0–100%，精度保留1位小数
     */
    @Column(precision = 5, scale = 1)
    private Double germinationRate;

    /**
     * 保质期：不得早于当前日期+6个月
     */
    private LocalDateTime shelfLife;

    /**
     * 播种季节：SPRING-春播，SUMMER-夏播，AUTUMN-秋播
     */
    @Column(length = 20)
    private String sowingSeason;

    /**
     * 品种描述
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * 抗病虫害等级：HIGH-高抗，MEDIUM-中抗，LOW-低抗
     */
    @Column(length = 20)
    private String diseaseResistance;

    /**
     * 状态：ACTIVE-可用，INACTIVE-停用
     */
    @Column(nullable = false, length = 20)
    private String status;

    /**
     * 创建人ID
     */
    private Long createdBy;

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
        if (status == null) {
            status = "ACTIVE";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public String getVariety() {
        return this.cropName;
    }

    public LocalDateTime getCreatedTime() {
        return this.createdAt;
    }
}
