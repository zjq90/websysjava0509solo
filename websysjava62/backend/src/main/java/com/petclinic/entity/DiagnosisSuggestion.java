package com.petclinic.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * AI诊断建议实体类
 * 用于存储AI模型分析问诊记录后生成的诊断建议
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "diagnosis_suggestion")
@Schema(description = "AI诊断建议")
public class DiagnosisSuggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "建议ID")
    private Long id;

    @Schema(description = "问诊记录ID")
    private Long consultationId;

    @Column(length = 100)
    @Schema(description = "问诊编号")
    private String consultationNo;

    @Column(length = 500)
    @Schema(description = "AI模型版本")
    private String aiModelVersion;

    @Column(length = 2000)
    @Schema(description = "可能的疾病列表（JSON格式）")
    private String possibleDiseases;

    @Column(length = 2000)
    @Schema(description = "AI诊断建议")
    private String suggestion;

    @Column(length = 2000)
    @Schema(description = "推荐检查项目")
    private String recommendedTests;

    @Column(length = 2000)
    @Schema(description = "推荐治疗方案")
    private String recommendedTreatment;

    @Column(precision = 5, scale = 2)
    @Schema(description = "诊断置信度（百分比）")
    private java.math.BigDecimal confidence;

    @Column(length = 500)
    @Schema(description = "风险提示")
    private String riskWarning;

    @Column(length = 20)
    @Schema(description = "建议状态：GENERATED-已生成, REVIEWED-已审核, ADOPTED-已采纳")
    private String status;

    @Column(length = 200)
    @Schema(description = "审核人")
    private String reviewedBy;

    @Schema(description = "审核时间")
    private LocalDateTime reviewedTime;

    @Schema(description = "生成时间")
    private LocalDateTime generateTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        if (generateTime == null) {
            generateTime = LocalDateTime.now();
        }
        if (status == null) {
            status = "GENERATED";
        }
    }
}
