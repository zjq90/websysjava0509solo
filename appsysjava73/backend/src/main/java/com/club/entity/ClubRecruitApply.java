package com.club.entity;

import com.club.entity.enums.ApplyStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 社团招新申请实体类
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "t_club_recruit_apply")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "社团招新申请信息")
public class ClubRecruitApply extends BaseEntity {

    @Schema(description = "招新ID", example = "1")
    @Column(name = "recruit_id", nullable = false)
    private Long recruitId;

    @Schema(description = "社团ID", example = "1")
    @Column(name = "club_id", nullable = false)
    private Long clubId;

    @Schema(description = "社团名称", example = "计算机协会")
    @Column(name = "club_name", length = 100)
    private String clubName;

    @Schema(description = "申请人ID", example = "1")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Schema(description = "申请人用户名", example = "student001")
    @Column(name = "username", length = 50)
    private String username;

    @Schema(description = "申请人真实姓名", example = "张三")
    @Column(name = "real_name", length = 50)
    private String realName;

    @Schema(description = "申请人头像", example = "https://example.com/avatar.png")
    @Column(name = "avatar", length = 500)
    private String avatar;

    @Schema(description = "学号", example = "2021001001")
    @Column(name = "student_no", length = 50)
    private String studentNo;

    @Schema(description = "手机号", example = "13800138000")
    @Column(name = "phone", length = 20)
    private String phone;

    @Schema(description = "院系", example = "计算机科学与技术学院")
    @Column(name = "college", length = 100)
    private String college;

    @Schema(description = "专业", example = "软件工程")
    @Column(name = "major", length = 100)
    private String major;

    @Schema(description = "年级", example = "2021级")
    @Column(name = "grade", length = 50)
    private String grade;

    @Schema(description = "意向部门ID", example = "1")
    @Column(name = "department_id")
    private Long departmentId;

    @Schema(description = "意向部门名称", example = "技术部")
    @Column(name = "department_name", length = 50)
    private String departmentName;

    @Schema(description = "个人简介", example = "热爱编程，有Java基础，希望加入技术部")
    @Column(name = "personal_intro", length = 2000)
    private String personalIntro;

    @Schema(description = "申请状态：PENDING-待审核 APPROVED-已通过 REJECTED-已驳回", example = "PENDING")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private ApplyStatusEnum status = ApplyStatusEnum.PENDING;

    @Schema(description = "审核留言", example = "欢迎加入！请参加面试")
    @Column(name = "review_message", length = 500)
    private String reviewMessage;

    @Schema(description = "审核人ID", example = "2")
    @Column(name = "reviewer_id")
    private Long reviewerId;

    @Schema(description = "审核人姓名", example = "李四")
    @Column(name = "reviewer_name", length = 50)
    private String reviewerName;

    @Schema(description = "审核时间", example = "2024-03-15 10:30:00")
    @Column(name = "review_time", length = 50)
    private String reviewTime;
}
