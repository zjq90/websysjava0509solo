package com.club.management.entity;

import com.club.management.entity.enums.RegistrationStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 活动报名实体类
 * 
 * @author club-management
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "registration", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"activity_id", "user_id"})
})
public class Registration extends BaseEntity {

    /**
     * 活动ID
     */
    @Column(name = "activity_id", nullable = false)
    private Long activityId;

    /**
     * 用户ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 学号（冗余，便于查询导出）
     */
    @Column(name = "student_no", length = 20)
    private String studentNo;

    /**
     * 姓名（冗余，便于查询导出）
     */
    @Column(name = "real_name", length = 50)
    private String realName;

    /**
     * 院系（冗余，便于查询导出）
     */
    @Column(name = "department", length = 100)
    private String department;

    /**
     * 专业（冗余，便于查询导出）
     */
    @Column(name = "major", length = 100)
    private String major;

    /**
     * 班级（冗余，便于查询导出）
     */
    @Column(name = "class_name", length = 50)
    private String className;

    /**
     * 手机号（冗余，便于联系）
     */
    @Column(name = "phone", length = 20)
    private String phone;

    /**
     * 报名状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private RegistrationStatus status = RegistrationStatus.PENDING;

    /**
     * 审核时间
     */
    @Column(name = "audit_time")
    private LocalDateTime auditTime;

    /**
     * 审核人ID
     */
    @Column(name = "audit_by")
    private Long auditBy;

    /**
     * 审核意见
     */
    @Column(name = "audit_remark", length = 500)
    private String auditRemark;

    /**
     * 报名备注
     */
    @Column(name = "remark", length = 500)
    private String remark;

    /**
     * 是否已签到
     */
    @Column(name = "signed_in", nullable = false)
    private Boolean signedIn = false;

    /**
     * 签到时间
     */
    @Column(name = "sign_in_time")
    private LocalDateTime signInTime;
}
