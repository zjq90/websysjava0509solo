package com.breeding.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * 土壤数据实体类
 * 用于记录田间试验地点的土壤数据，包括土壤类型、pH值、养分含量等
 * 用于分析土壤环境对育种的影响
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "soil_data")
public class SoilData {

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
     * 土壤类型：壤土、砂土、黏土、黑土、红壤等
     */
    @Column(length = 50)
    private String soilType;

    /**
     * 土壤质地：重壤、中壤、轻壤、砂壤等
     */
    @Column(length = 50)
    private String soilTexture;

    /**
     * 土壤pH值
     */
    private Double phValue;

    /**
     * 有机质含量（g/kg）
     */
    private Double organicMatter;

    /**
     * 全氮含量（g/kg）
     */
    private Double totalNitrogen;

    /**
     * 有效磷含量（mg/kg）
     */
    private Double availablePhosphorus;

    /**
     * 速效钾含量（mg/kg）
     */
    private Double availablePotassium;

    /**
     * 全磷含量（g/kg）
     */
    private Double totalPhosphorus;

    /**
     * 全钾含量（g/kg）
     */
    private Double totalPotassium;

    /**
     * 碱解氮含量（mg/kg）
     */
    private Double alkalineHydrolyzableN;

    /**
     * 交换性钙含量（cmol/kg）
     */
    private Double exchangeableCalcium;

    /**
     * 交换性镁含量（cmol/kg）
     */
    private Double exchangeableMagnesium;

    /**
     * 有效硫含量（mg/kg）
     */
    private Double availableSulfur;

    /**
     * 有效锌含量（mg/kg）
     */
    private Double availableZinc;

    /**
     * 有效硼含量（mg/kg）
     */
    private Double availableBoron;

    /**
     * 有效锰含量（mg/kg）
     */
    private Double availableManganese;

    /**
     * 有效铜含量（mg/kg）
     */
    private Double availableCopper;

    /**
     * 有效铁含量（mg/kg）
     */
    private Double availableIron;

    /**
     * 阳离子交换量（cmol/kg）
     */
    private Double cationExchangeCapacity;

    /**
     * 容重（g/cm³）
     */
    private Double bulkDensity;

    /**
     * 孔隙度（%）
     */
    private Double porosity;

    /**
     * 土壤含水量（%）
     */
    private Double moistureContent;

    /**
     * 电导率（mS/cm）
     */
    private Double electricalConductivity;

    /**
     * 盐分含量（g/kg）
     */
    private Double saltContent;

    /**
     * 综合评价
     */
    @Column(length = 1000)
    private String overallEvaluation;

    /**
     * 改良建议
     */
    @Column(length = 1000)
    private String improvementSuggestion;

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
