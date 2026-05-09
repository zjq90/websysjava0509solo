package com.traceability.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 质检报告实体类
 * 记录质检报告信息，与种子批次关联
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "quality_report")
public class QualityReport extends BaseEntity {

    @Column(name = "report_no", nullable = false, unique = true, length = 50)
    private String reportNo;

    @Column(name = "batch_no", nullable = false, length = 50)
    private String batchNo;

    @Column(name = "test_date", length = 20)
    private String testDate;

    @Column(name = "test_institution", length = 100)
    private String testInstitution;

    @Column(name = "test_items", length = 200)
    private String testItems;

    @Column(name = "germination_rate", precision = 5, scale = 2)
    private BigDecimal germinationRate;

    @Column(name = "purity", precision = 5, scale = 2)
    private BigDecimal purity;

    @Column(name = "clarity", precision = 5, scale = 2)
    private BigDecimal clarity;

    @Column(name = "moisture", precision = 5, scale = 2)
    private BigDecimal moisture;

    @Column(name = "thousand_grain_weight", precision = 10, scale = 2)
    private BigDecimal thousandGrainWeight;

    @Column(name = "health_degree", precision = 5, scale = 2)
    private BigDecimal healthDegree;

    @Column(name = "vigor_index", precision = 10, scale = 2)
    private BigDecimal vigorIndex;

    @Column(name = "test_result", length = 20)
    private String testResult;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "tester", length = 50)
    private String tester;

    @Column(name = "report_file_url", length = 500)
    private String reportFileUrl;

    @Column(name = "remark", length = 500)
    private String remark;
}
