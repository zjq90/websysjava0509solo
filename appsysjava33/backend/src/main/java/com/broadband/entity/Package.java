package com.broadband.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 套餐实体类
 * 存储宽带套餐信息
 * 
 * @author broadband
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "biz_package")
@Schema(description = "套餐信息")
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "套餐ID")
    private Long id;

    @Schema(description = "套餐名称")
    @Column(length = 100, nullable = false)
    private String name;

    @Schema(description = "套餐类型: 1-基础套餐 2-提速包 3-安全防护包")
    @Column(nullable = false)
    private Integer type;

    @Schema(description = "带宽(Mbps)")
    private Integer bandwidth;

    @Schema(description = "月费(元)")
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal monthlyFee;

    @Schema(description = "年费(元)")
    @Column(precision = 10, scale = 2)
    private BigDecimal yearlyFee;

    @Schema(description = "套餐描述")
    @Column(length = 1000)
    private String description;

    @Schema(description = "套餐特点(JSON格式)")
    @Column(length = 2000)
    private String features;

    @Schema(description = "是否支持升级: 0-否 1-是")
    @Column(nullable = false, columnDefinition = "int default 1")
    private Integer canUpgrade = 1;

    @Schema(description = "是否支持降档: 0-否 1-是")
    @Column(nullable = false, columnDefinition = "int default 1")
    private Integer canDowngrade = 1;

    @Schema(description = "父套餐ID(用于增值包)")
    private Long parentId;

    @Schema(description = "套餐状态: 0-下架 1-上架")
    @Column(nullable = false, columnDefinition = "int default 1")
    private Integer status = 1;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "创建时间")
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
