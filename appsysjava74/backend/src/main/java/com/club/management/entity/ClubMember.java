package com.club.management.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 社团成员实体类
 * 
 * @author club-management
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "club_member", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"club_id", "user_id"})
})
public class ClubMember extends BaseEntity {

    /**
     * 社团ID
     */
    @Column(name = "club_id", nullable = false)
    private Long clubId;

    /**
     * 用户ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 学号（冗余）
     */
    @Column(name = "student_no", length = 20)
    private String studentNo;

    /**
     * 姓名（冗余）
     */
    @Column(name = "real_name", length = 50)
    private String realName;

    /**
     * 院系（冗余）
     */
    @Column(name = "department", length = 100)
    private String department;

    /**
     * 专业（冗余）
     */
    @Column(name = "major", length = 100)
    private String major;

    /**
     * 班级（冗余）
     */
    @Column(name = "class_name", length = 50)
    private String className;

    /**
     * 职位（社长、副社长、部长、副部长、干事、普通成员等）
     */
    @Column(name = "position", length = 50)
    private String position;

    /**
     * 加入时间
     */
    @Column(name = "join_time")
    private LocalDateTime joinTime;

    /**
     * 退出时间
     */
    @Column(name = "leave_time")
    private LocalDateTime leaveTime;

    /**
     * 成员状态（0：审核中，1：已加入，2：已退出，3：已拒绝）
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    /**
     * 申请理由
     */
    @Column(name = "apply_reason", length = 500)
    private String applyReason;
}
