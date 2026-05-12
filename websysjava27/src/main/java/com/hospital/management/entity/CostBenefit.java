package com.hospital.management.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 成本效益实体类
 * 记录科室成本、项目成本、药品及耗材使用成本等数据
 *
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cost_benefit")
public class CostBenefit {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 统计日期
     */
    @Column(name = "stat_date", nullable = false)
    private LocalDate statDate;

    /**
     * 科室ID
     */
    @Column(name = "department_id")
    private Long departmentId;

    /**
     * 科室名称
     */
    @Column(name = "department_name", length = 100)
    private String departmentName;

    /**
     * 人力成本(元)
     */
    @Column(name = "labor_cost")
    private Double laborCost;

    /**
     * 药品成本(元)
     */
    @Column(name = "medicine_cost")
    private Double medicineCost;

    /**
     * 耗材成本(元)
     */
    @Column(name = "consumable_cost")
    private Double consumableCost;

    /**
     * 设备折旧成本(元)
     */
    @Column(name = "equipment_depreciation")
    private Double equipmentDepreciation;

    /**
     * 水电能耗成本(元)
     */
    @Column(name = "utility_cost")
    private Double utilityCost;

    /**
     * 其他成本(元)
     */
    @Column(name = "other_cost")
    private Double otherCost;

    /**
     * 总成本(元)
     */
    @Column(name = "total_cost")
    private Double totalCost;

    /**
     * 医疗收入(元)
     */
    @Column(name = "medical_income")
    private Double medicalIncome;

    /**
     * 药品收入(元)
     */
    @Column(name = "medicine_income")
    private Double medicineIncome;

    /**
     * 检查收入(元)
     */
    @Column(name = "examination_income")
    private Double examinationIncome;

    /**
     * 其他收入(元)
     */
    @Column(name = "other_income")
    private Double otherIncome;

    /**
     * 总收入(元)
     */
    @Column(name = "total_income")
    private Double totalIncome;

    /**
     * 利润(元)
     */
    @Column(name = "profit")
    private Double profit;

    /**
     * 利润率(%)
     */
    @Column(name = "profit_margin")
    private Double profitMargin;

    /**
     * 成本收益率(%)
     */
    @Column(name = "cost_benefit_ratio")
    private Double costBenefitRatio;

    /**
     * 人均创收(元)
     */
    @Column(name = "income_per_capita")
    private Double incomePerCapita;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
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
