package com.hospital.management.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 医疗质量实体类
 * 记录病历质量、合理用药、院内感染、不良事件等医疗质量数据
 *
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "medical_quality")
public class MedicalQuality {

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
     * 病历总数
     */
    @Column(name = "total_records")
    private Integer totalRecords;

    /**
     * 甲级病历数
     */
    @Column(name = "grade_a_records")
    private Integer gradeARecords;

    /**
     * 乙级病历数
     */
    @Column(name = "grade_b_records")
    private Integer gradeBRecords;

    /**
     * 丙级病历数
     */
    @Column(name = "grade_c_records")
    private Integer gradeCRecords;

    /**
     * 病历合格率(%)
     */
    @Column(name = "record_qualification_rate")
    private Double recordQualificationRate;

    /**
     * 处方总数
     */
    @Column(name = "total_prescriptions")
    private Integer totalPrescriptions;

    /**
     * 不合理处方数
     */
    @Column(name = "unreasonable_prescriptions")
    private Integer unreasonablePrescriptions;

    /**
     * 合理用药率(%)
     */
    @Column(name = "rational_drug_use_rate")
    private Double rationalDrugUseRate;

    /**
     * 抗菌药物使用强度(DDDs)
     */
    @Column(name = "antibiotic_use_intensity")
    private Double antibioticUseIntensity;

    /**
     * 院内感染例数
     */
    @Column(name = "nosocomial_infection_count")
    private Integer nosocomialInfectionCount;

    /**
     * 院内感染率(%)
     */
    @Column(name = "nosocomial_infection_rate")
    private Double nosocomialInfectionRate;

    /**
     * 不良事件数
     */
    @Column(name = "adverse_event_count")
    private Integer adverseEventCount;

    /**
     * 严重不良事件数
     */
    @Column(name = "serious_adverse_event_count")
    private Integer seriousAdverseEventCount;

    /**
     * 不良事件上报率(%)
     */
    @Column(name = "adverse_event_report_rate")
    private Double adverseEventReportRate;

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
