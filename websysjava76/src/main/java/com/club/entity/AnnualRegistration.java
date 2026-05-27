package com.club.entity;

import com.club.enums.ApprovalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 社团年度注册实体类
 * 存储社团年度注册申请信息
 *
 * @author Club Management System
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "annual_registration")
@Schema(description = "年度注册")
public class AnnualRegistration extends BaseEntity {

    /**
     * 关联社团ID
     */
    @Column(name = "club_id", nullable = false)
    @Schema(description = "社团ID", example = "1")
    private Long clubId;

    /**
     * 社团名称
     */
    @Column(name = "club_name", length = 100)
    @Schema(description = "社团名称", example = "计算机协会")
    private String clubName;

    /**
     * 注册年份
     */
    @Column(name = "register_year", nullable = false)
    @Schema(description = "注册年份", example = "2024")
    private Integer registerYear;

    /**
     * 年度总结
     */
    @Column(name = "annual_summary", columnDefinition = "TEXT")
    @Schema(description = "年度总结", example = "本年度共举办活动15次...")
    private String annualSummary;

    /**
     * 下年度计划
     */
    @Column(name = "next_year_plan", columnDefinition = "TEXT")
    @Schema(description = "下年度计划", example = "下年度计划举办活动20次...")
    private String nextYearPlan;

    /**
     * 年度活动数量
     */
    @Column(name = "activity_count")
    @Schema(description = "年度活动数量", example = "15")
    private Integer activityCount;

    /**
     * 成员人数变化
     */
    @Column(name = "member_change")
    @Schema(description = "成员人数变化", example = "50")
    private Integer memberChange;

    /**
     * 财务收支情况
     */
    @Column(name = "finance_report", columnDefinition = "TEXT")
    @Schema(description = "财务收支情况", example = "年度收入5000元，支出3000元...")
    private String financeReport;

    /**
     * 存在的问题与建议
     */
    @Column(name = "problems", length = 1000)
    @Schema(description = "存在的问题与建议", example = "活动场地不足...")
    private String problems;

    /**
     * 申请状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    @Schema(description = "审核状态", example = "PENDING")
    private ApprovalStatus status = ApprovalStatus.PENDING;

    /**
     * 审核人ID
     */
    @Column(name = "approver_id")
    @Schema(description = "审核人ID", example = "1")
    private Long approverId;

    /**
     * 审核人姓名
     */
    @Column(name = "approver_name", length = 50)
    @Schema(description = "审核人姓名", example = "管理员")
    private String approverName;

    /**
     * 审核时间
     */
    @Column(name = "approve_time")
    @Schema(description = "审核时间")
    private LocalDateTime approveTime;

    /**
     * 审核意见
     */
    @Column(name = "approve_opinion", length = 500)
    @Schema(description = "审核意见", example = "同意注册")
    private String approveOpinion;
}
