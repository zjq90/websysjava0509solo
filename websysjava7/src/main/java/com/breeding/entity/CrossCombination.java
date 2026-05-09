package com.breeding.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 杂交组合实体类
 * 用于记录亲本杂交的组合信息，包括父本、母本、杂交时间、后代情况等
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cross_combinations")
public class CrossCombination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 杂交组合编号
     */
    @Column(nullable = false, unique = true, length = 50)
    private String combinationCode;

    /**
     * 组合名称
     */
    @Column(nullable = false, length = 200)
    private String combinationName;

    /**
     * 母本信息
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "female_parent_id")
    private ParentPlant femaleParent;

    /**
     * 父本信息
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "male_parent_id")
    private ParentPlant maleParent;

    /**
     * 杂交日期
     */
    @Column(nullable = false)
    private LocalDate crossDate;

    /**
     * 杂交地点
     */
    @Column(length = 200)
    private String crossLocation;

    /**
     * 杂交方式：人工杂交、自然杂交、组培等
     */
    @Column(length = 50)
    private String crossMethod;

    /**
     * 杂交目的
     */
    @Column(length = 500)
    private String crossPurpose;

    /**
     * 杂交穗数/花朵数
     */
    private Integer flowerCount;

    /**
     * 结实率（%）
     */
    private Double seedSetRate;

    /**
     * 获得种子数
     */
    private Integer seedCount;

    /**
     * 后代世代：F1、F2、F3等
     */
    @Column(length = 10)
    private String generation;

    /**
     * 组合状态：进行中、已完成、已淘汰
     */
    @Column(nullable = false, length = 20)
    private String status;

    /**
     * 后代主要性状
     */
    @Column(length = 1000)
    private String offspringTraits;

    /**
     * 综合评价
     */
    @Column(length = 1000)
    private String evaluation;

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
     * 与田间试验的关联关系
     */
    @OneToMany(mappedBy = "crossCombination", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FieldExperiment> experiments = new ArrayList<>();

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
