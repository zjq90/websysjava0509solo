package com.breeding.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * 亲本植物实体类
 * 用于记录育种材料的亲本信息，包括品种名称、来源、性状特征等
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "parent_plants")
public class ParentPlant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 亲本编号
     */
    @Column(nullable = false, unique = true, length = 50)
    private String parentCode;

    /**
     * 品种名称
     */
    @Column(nullable = false, length = 200)
    private String varietyName;

    /**
     * 亲本类型：父本、母本
     */
    @Column(nullable = false, length = 10)
    private String parentType;

    /**
     * 来源地
     */
    @Column(length = 200)
    private String origin;

    /**
     * 作物类型
     */
    @Column(nullable = false, length = 100)
    private String cropType;

    /**
     * 世代信息：F0、F1、F2、稳定系等
     */
    @Column(length = 20)
    private String generation;

    /**
     * 主要性状特征
     */
    @Column(length = 1000)
    private String traits;

    /**
     * 品质性状
     */
    @Column(length = 500)
    private String qualityTraits;

    /**
     * 抗病性
     */
    @Column(length = 200)
    private String diseaseResistance;

    /**
     * 抗逆性
     */
    @Column(length = 200)
    private String stressTolerance;

    /**
     * 产量潜力（公斤/亩）
     */
    private Double yieldPotential;

    /**
     * 生育期（天）
     */
    private Integer growthPeriod;

    /**
     * 收集/引入日期
     */
    private LocalDate collectionDate;

    /**
     * 保存位置
     */
    @Column(length = 200)
    private String storageLocation;

    /**
     * 备注
     */
    @Column(length = 1000)
    private String remarks;

    /**
     * 备注（别名，用于兼容）
     */
    @Column(length = 1000)
    private String notes;

    /**
     * 关联的育种项目
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private BreedingProject project;

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
