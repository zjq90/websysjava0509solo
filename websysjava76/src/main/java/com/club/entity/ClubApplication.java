package com.club.entity;

import com.club.enums.ApprovalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 社团成立申请实体类
 * 存储学生提交的社团成立申请信息
 *
 * @author Club Management System
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "club_application")
@Schema(description = "社团成立申请")
public class ClubApplication extends BaseEntity {

    /**
     * 申请社团名称
     */
    @Column(name = "club_name", nullable = false, length = 100)
    @Schema(description = "社团名称", example = "人工智能协会")
    private String clubName;

    /**
     * 社团类型
     */
    @Column(name = "club_type", length = 50)
    @Schema(description = "社团类型", example = "ACADEMIC")
    private String clubType;

    /**
     * 所属院系
     */
    @Column(name = "department", length = 100)
    @Schema(description = "所属院系", example = "计算机学院")
    private String department;

    /**
     * 社团简介
     */
    @Column(name = "description", length = 1000)
    @Schema(description = "社团简介", example = "致力于人工智能技术的学习与实践")
    private String description;

    /**
     * 社团章程
     */
    @Column(name = "constitution", columnDefinition = "TEXT")
    @Schema(description = "社团章程", example = "第一章 总则...")
    private String constitution;

    /**
     * 发起人姓名
     */
    @Column(name = "initiator_name", length = 50)
    @Schema(description = "发起人姓名", example = "王五")
    private String initiatorName;

    /**
     * 发起人学号
     */
    @Column(name = "initiator_student_id", length = 30)
    @Schema(description = "发起人学号", example = "2021001")
    private String initiatorStudentId;

    /**
     * 发起人联系电话
     */
    @Column(name = "initiator_phone", length = 20)
    @Schema(description = "发起人联系电话", example = "13800138002")
    private String initiatorPhone;

    /**
     * 发起人邮箱
     */
    @Column(name = "initiator_email", length = 100)
    @Schema(description = "发起人邮箱", example = "wangwu@club.edu.cn")
    private String initiatorEmail;

    /**
     * 指导老师姓名
     */
    @Column(name = "advisor_name", length = 50)
    @Schema(description = "指导老师姓名", example = "李教授")
    private String advisorName;

    /**
     * 指导老师意见
     */
    @Column(name = "advisor_opinion", length = 500)
    @Schema(description = "指导老师意见", example = "同意指导")
    private String advisorOpinion;

    /**
     * 发起人数量
     */
    @Column(name = "initiator_count")
    @Schema(description = "发起人数量", example = "8")
    private Integer initiatorCount;

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
    @Schema(description = "审核意见", example = "同意成立")
    private String approveOpinion;

    /**
     * 关联的社团ID（审核通过后生成）
     */
    @Column(name = "club_id")
    @Schema(description = "关联社团ID", example = "10")
    private Long clubId;
}
