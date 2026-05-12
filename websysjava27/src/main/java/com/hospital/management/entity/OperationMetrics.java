package com.hospital.management.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 运营指标实体类
 * 记录医院门诊量、住院量、手术量、床位使用率等关键运营指标
 *
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "operation_metrics")
public class OperationMetrics {

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
     * 门诊量
     */
    @Column(name = "outpatient_count")
    private Integer outpatientCount;

    /**
     * 住院量
     */
    @Column(name = "inpatient_count")
    private Integer inpatientCount;

    /**
     * 手术量
     */
    @Column(name = "surgery_count")
    private Integer surgeryCount;

    /**
     * 床位总数
     */
    @Column(name = "total_beds")
    private Integer totalBeds;

    /**
     * 实际使用床位
     */
    @Column(name = "used_beds")
    private Integer usedBeds;

    /**
     * 床位使用率(%)
     */
    @Column(name = "bed_usage_rate")
    private Double bedUsageRate;

    /**
     * 平均住院日(天)
     */
    @Column(name = "avg_hospitalization_days")
    private Double avgHospitalizationDays;

    /**
     * 药品收入(元)
     */
    @Column(name = "medicine_income")
    private Double medicineIncome;

    /**
     * 耗材收入(元)
     */
    @Column(name = "consumable_income")
    private Double consumableIncome;

    /**
     * 总收入(元)
     */
    @Column(name = "total_income")
    private Double totalIncome;

    /**
     * 药占比(%)
     */
    @Column(name = "medicine_ratio")
    private Double medicineRatio;

    /**
     * 耗占比(%)
     */
    @Column(name = "consumable_ratio")
    private Double consumableRatio;

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
