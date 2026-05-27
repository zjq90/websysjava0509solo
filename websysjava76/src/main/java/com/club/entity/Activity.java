package com.club.entity;

import com.club.enums.ActivityType;
import com.club.enums.ApprovalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 活动实体类
 * 存储社团活动信息
 *
 * @author Club Management System
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "activity")
@Schema(description = "活动信息")
public class Activity extends BaseEntity {

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
     * 活动名称
     */
    @Column(name = "name", nullable = false, length = 200)
    @Schema(description = "活动名称", example = "2024年编程大赛")
    private String name;

    /**
     * 活动类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    @Schema(description = "活动类型", example = "SCHOOL_LEVEL")
    private ActivityType type;

    /**
     * 活动主题
     */
    @Column(name = "theme", length = 200)
    @Schema(description = "活动主题", example = "创新编程，智慧未来")
    private String theme;

    /**
     * 活动描述
     */
    @Column(name = "description", columnDefinition = "TEXT")
    @Schema(description = "活动描述", example = "本次活动旨在提高学生编程能力...")
    private String description;

    /**
     * 活动海报URL
     */
    @Column(name = "poster_url", length = 500)
    @Schema(description = "活动海报URL", example = "http://example.com/poster.jpg")
    private String posterUrl;

    /**
     * 活动开始时间
     */
    @Column(name = "start_time")
    @Schema(description = "活动开始时间", example = "2024-06-15T09:00:00")
    private LocalDateTime startTime;

    /**
     * 活动结束时间
     */
    @Column(name = "end_time")
    @Schema(description = "活动结束时间", example = "2024-06-15T18:00:00")
    private LocalDateTime endTime;

    /**
     * 活动地点
     */
    @Column(name = "location", length = 200)
    @Schema(description = "活动地点", example = "学校大礼堂")
    private String location;

    /**
     * 预计参与人数
     */
    @Column(name = "expected_participants")
    @Schema(description = "预计参与人数", example = "200")
    private Integer expectedParticipants;

    /**
     * 实际参与人数
     */
    @Column(name = "actual_participants")
    @Schema(description = "实际参与人数", example = "180")
    private Integer actualParticipants;

    /**
     * 活动预算
     */
    @Column(name = "budget", precision = 10, scale = 2)
    @Schema(description = "活动预算", example = "5000.00")
    private BigDecimal budget;

    /**
     * 负责人姓名
     */
    @Column(name = "organizer_name", length = 50)
    @Schema(description = "负责人姓名", example = "赵六")
    private String organizerName;

    /**
     * 负责人联系电话
     */
    @Column(name = "organizer_phone", length = 20)
    @Schema(description = "负责人联系电话", example = "13800138003")
    private String organizerPhone;

    /**
     * 是否需要审批
     */
    @Column(name = "need_approval")
    @Schema(description = "是否需要审批", example = "true")
    private Boolean needApproval = false;

    /**
     * 审核状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status", length = 30)
    @Schema(description = "审核状态", example = "APPROVED")
    private ApprovalStatus approvalStatus;

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
    @Schema(description = "审核意见", example = "同意举办")
    private String approveOpinion;

    /**
     * 内容是否合规
     */
    @Column(name = "content_compliant")
    @Schema(description = "内容是否合规", example = "true")
    private Boolean contentCompliant = true;

    /**
     * 违规内容说明
     */
    @Column(name = "violation_note", length = 500)
    @Schema(description = "违规内容说明")
    private String violationNote;

    /**
     * 活动状态（0-未开始，1-进行中，2-已结束，3-已取消）
     */
    @Column(name = "status")
    @Schema(description = "活动状态", example = "0")
    private Integer status = 0;

    /**
     * 是否为大型活动（预计人数>=500人为大型活动）
     */
    @Column(name = "is_large_scale")
    @Schema(description = "是否为大型活动", example = "false")
    private Boolean isLargeScale = false;

    /**
     * 审批级别（1-社联审核，2-团委审核，3-校级领导审核）
     */
    @Column(name = "approval_level")
    @Schema(description = "审批级别", example = "1")
    private Integer approvalLevel = 1;

    /**
     * 当前审批阶段
     */
    @Column(name = "current_approval_stage")
    @Schema(description = "当前审批阶段", example = "1")
    private Integer currentApprovalStage = 1;

    /**
     * 社联审核人ID
     */
    @Column(name = "association_approver_id")
    @Schema(description = "社联审核人ID")
    private Long associationApproverId;

    /**
     * 社联审核人姓名
     */
    @Column(name = "association_approver_name")
    @Schema(description = "社联审核人姓名")
    private String associationApproverName;

    /**
     * 社联审核时间
     */
    @Column(name = "association_approve_time")
    @Schema(description = "社联审核时间")
    private LocalDateTime associationApproveTime;

    /**
     * 社联审核意见
     */
    @Column(name = "association_approve_opinion")
    @Schema(description = "社联审核意见")
    private String associationApproveOpinion;

    /**
     * 团委审核人ID
     */
    @Column(name = "league_approver_id")
    @Schema(description = "团委审核人ID")
    private Long leagueApproverId;

    /**
     * 团委审核人姓名
     */
    @Column(name = "league_approver_name")
    @Schema(description = "团委审核人姓名")
    private String leagueApproverName;

    /**
     * 团委审核时间
     */
    @Column(name = "league_approve_time")
    @Schema(description = "团委审核时间")
    private LocalDateTime leagueApproveTime;

    /**
     * 团委审核意见
     */
    @Column(name = "league_approve_opinion")
    @Schema(description = "团委审核意见")
    private String leagueApproveOpinion;

    /**
     * 校级领导审核人ID
     */
    @Column(name = "school_approver_id")
    @Schema(description = "校级领导审核人ID")
    private Long schoolApproverId;

    /**
     * 校级领导审核人姓名
     */
    @Column(name = "school_approver_name")
    @Schema(description = "校级领导审核人姓名")
    private String schoolApproverName;

    /**
     * 校级领导审核时间
     */
    @Column(name = "school_approve_time")
    @Schema(description = "校级领导审核时间")
    private LocalDateTime schoolApproveTime;

    /**
     * 校级领导审核意见
     */
    @Column(name = "school_approve_opinion")
    @Schema(description = "校级领导审核意见")
    private String schoolApproveOpinion;

    /**
     * 参与高校（跨校活动专用）
     */
    @Column(name = "participating_schools", length = 500)
    @Schema(description = "参与高校")
    private String participatingSchools;

    /**
     * 跨校活动联系人
     */
    @Column(name = "cross_school_contact", length = 100)
    @Schema(description = "跨校活动联系人")
    private String crossSchoolContact;

    /**
     * 跨校活动联系电话
     */
    @Column(name = "cross_school_phone", length = 20)
    @Schema(description = "跨校活动联系电话")
    private String crossSchoolPhone;

    /**
     * 安全预案（大型活动专用）
     */
    @Column(name = "safety_plan", columnDefinition = "TEXT")
    @Schema(description = "安全预案")
    private String safetyPlan;

    /**
     * 备注
     */
    @Column(name = "remark", length = 500)
    @Schema(description = "备注")
    private String remark;
}
