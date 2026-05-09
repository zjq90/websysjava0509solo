package com.agriculture.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 地块实体类
 * 存储试验地块基本信息，用于田间数据采集时关联
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "plots")
public class Plot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 地块编号，唯一标识
     */
    @Column(nullable = false, unique = true, length = 20)
    private String plotCode;

    /**
     * 地块名称
     */
    @Column(nullable = false, length = 100)
    private String plotName;

    /**
     * 地块所在区域/省份
     */
    @Column(length = 50)
    private String province;

    /**
     * 地块所在城市
     */
    @Column(length = 50)
    private String city;

    /**
     * 地块所在区县
     */
    @Column(length = 50)
    private String district;

    /**
     * 详细地址
     */
    @Column(length = 255)
    private String address;

    /**
     * 纬度
     */
    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;

    /**
     * 经度
     */
    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;

    /**
     * 地块面积（亩）
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal area;

    /**
     * 土壤类型
     */
    @Column(length = 50)
    private String soilType;

    /**
     * 灌溉条件：GOOD-良好，MEDIUM-一般，POOR-较差
     */
    @Column(length = 20)
    private String irrigationCondition;

    /**
     * 责任人
     */
    @Column(length = 50)
    private String responsiblePerson;

    /**
     * 地块描述
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * 状态：ACTIVE-使用中，INACTIVE-闲置
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

    public String getLocation() {
        if (this.address != null && !this.address.isEmpty()) {
            return this.address;
        }
        StringBuilder sb = new StringBuilder();
        if (this.province != null) sb.append(this.province);
        if (this.city != null) sb.append(this.city);
        if (this.district != null) sb.append(this.district);
        return sb.length() > 0 ? sb.toString() : null;
    }
}
