package com.breeding.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * 田间试验数据实体类
 * 用于记录田间试验的生长数据、抗病性、产量表现等关键指标
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "field_experiments")
public class FieldExperiment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 试验编号
     */
    @Column(nullable = false, unique = true, length = 50)
    private String experimentCode;

    /**
     * 试验名称
     */
    @Column(nullable = false, length = 200)
    private String experimentName;

    /**
     * 试验地点
     */
    @Column(nullable = false, length = 200)
    private String location;

    /**
     * 试验年份
     */
    @Column(nullable = false)
    private Integer year;

    /**
     * 播种日期
     */
    private LocalDate sowingDate;

    /**
     * 收获日期
     */
    private LocalDate harvestDate;

    /**
     * 抽穗日期
     */
    private LocalDate headingDate;

    /**
     * 成熟期
     */
    private LocalDate maturityDate;

    /**
     * 生育期（天）
     */
    private Integer growthPeriod;

    /**
     * 株高（厘米）
     */
    private Double plantHeight;

    /**
     * 穗长/果荚长（厘米）
     */
    private Double earLength;

    /**
     * 每穗粒数/每荚粒数
     */
    private Integer grainsPerEar;

    /**
     * 千粒重（克）
     */
    private Double thousandGrainWeight;

    /**
     * 小区产量（公斤）
     */
    private Double plotYield;

    /**
     * 折合亩产（公斤/亩）
     */
    private Double yieldPerMu;

    /**
     * 白粉病抗性：高抗、抗、中抗、感、高感
     */
    @Column(length = 20)
    private String powderyMildewResistance;

    /**
     * 锈病抗性
     */
    @Column(length = 20)
    private String rustResistance;

    /**
     * 纹枯病抗性
     */
    @Column(length = 20)
    private String sheathBlightResistance;

    /**
     * 综合抗病性评价
     */
    @Column(length = 500)
    private String diseaseResistanceEvaluation;

    /**
     * 抗倒性：强、中、弱
     */
    @Column(length = 10)
    private String lodgingResistance;

    /**
     * 抗旱性：强、中、弱
     */
    @Column(length = 10)
    private String droughtResistance;

    /**
     * 抗寒性：强、中、弱
     */
    @Column(length = 10)
    private String coldResistance;

    /**
     * 品质指标：蛋白质含量（%）
     */
    private Double proteinContent;

    /**
     * 品质指标：淀粉含量（%）
     */
    private Double starchContent;

    /**
     * 品质指标：脂肪含量（%）
     */
    private Double fatContent;

    /**
     * 综合评价
     */
    @Column(length = 1000)
    private String overallEvaluation;

    /**
     * 试验状态：进行中、已完成
     */
    @Column(nullable = false, length = 20)
    private String status;

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
     * 关联的杂交组合
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cross_combination_id")
    private CrossCombination crossCombination;

    /**
     * 关联的亲本（可选）
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private ParentPlant parent;

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
