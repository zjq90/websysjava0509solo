package com.lims.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 检验检查报告实体类
 * 存储生成的检验/检查报告信息，支持多级审核
 *
 * @author LIMS Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "test_report")
@Schema(description = "检验检查报告")
public class TestReport {

    /**
     * 报告ID，主键自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "报告ID", example = "1")
    private Long id;

    /**
     * 报告编号，唯一标识
     */
    @Column(unique = true, nullable = false, length = 50)
    @Schema(description = "报告编号", example = "RPT20240101001")
    private String reportNo;

    /**
     * 关联申请ID
     */
    @Column(nullable = false)
    @Schema(description = "申请ID", example = "1")
    private Long applicationId;

    /**
     * 关联结果ID
     */
    @Column
    @Schema(description = "结果ID", example = "1")
    private Long resultId;

    /**
     * 报告标题
     */
    @Column(nullable = false, length = 200)
    @Schema(description = "报告标题", example = "血常规检验报告")
    private String reportTitle;

    /**
     * 报告内容（HTML或文本格式
     */
    @Column(columnDefinition = "TEXT")
    @Schema(description = "报告内容", example = "详细检验结果...")
    private String reportContent;

    /**
     * 检验/检查结论
     */
    @Column(length = 2000)
    @Schema(description = "检查结论", example = "各项指标正常")
    private String conclusion;

    /**
     * 建议
     */
    @Column(length = 1000)
    @Schema(description = "建议", example = "定期复查")
    private String suggestion;

    /**
     * 报告状态：DRAFT-草稿，FIRST_AUDIT-待一级审核，SECOND_AUDIT-待二级审核，PASSED-审核通过，REJECTED-审核驳回，PUBLISHED-已发布
     */
    @Column(nullable = false, length = 50)
    @Schema(description = "报告状态", example = "DRAFT")
    private String status;

    /**
     * 报告生成人ID
     */
    @Column(nullable = false)
    @Schema(description = "报告生成人ID", example = "2")
    private Long createdBy;

    /**
     * 一级审核人ID
     */
    @Column
    @Schema(description = "一级审核人ID", example = "3")
    private Long firstAuditorId;

    /**
     * 一级审核意见
     */
    @Column(length = 500)
    @Schema(description = "一级审核意见", example = "同意")
    private String firstAuditOpinion;

    /**
     * 一级审核时间
     */
    @Column
    @Schema(description = "一级审核时间")
    private LocalDateTime firstAuditTime;

    /**
     * 二级审核人ID
     */
    @Column
    @Schema(description = "二级审核人ID", example = "4")
    private Long secondAuditorId;

    /**
     * 二级审核意见
     */
    @Column(length = 500)
    @Schema(description = "二级审核意见", example = "同意发布")
    private String secondAuditOpinion;

    /**
     * 二级审核时间
     */
    @Column
    @Schema(description = "二级审核时间")
    private LocalDateTime secondAuditTime;

    /**
     * 发布人ID
     */
    @Column
    @Schema(description = "发布人ID", example = "4")
    private Long publishedBy;

    /**
     * 发布时间
     */
    @Column
    @Schema(description = "发布时间")
    private LocalDateTime publishedTime;

    /**
     * 患者是否已查看
     */
    @Column(nullable = false)
    @Schema(description = "患者已查看", example = "false")
    private Boolean patientViewed;

    /**
     * 患者查看时间
     */
    @Column
    @Schema(description = "患者查看时间")
    private LocalDateTime patientViewTime;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        status = "DRAFT";
        patientViewed = false;
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
