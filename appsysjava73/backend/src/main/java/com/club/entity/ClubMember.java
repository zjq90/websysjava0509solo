package com.club.entity;

import com.club.entity.enums.MemberRoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 社团成员实体类
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "t_club_member")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "社团成员信息")
public class ClubMember extends BaseEntity {

    @Schema(description = "社团ID", example = "1")
    @Column(name = "club_id", nullable = false)
    private Long clubId;

    @Schema(description = "用户ID", example = "1")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Schema(description = "用户名", example = "student001")
    @Column(name = "username", length = 50)
    private String username;

    @Schema(description = "真实姓名", example = "张三")
    @Column(name = "real_name", length = 50)
    private String realName;

    @Schema(description = "头像", example = "https://example.com/avatar.png")
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

    @Schema(description = "部门ID", example = "1")
    @Column(name = "department_id")
    private Long departmentId;

    @Schema(description = "部门名称", example = "技术部")
    @Column(name = "department_name", length = 50)
    private String departmentName;

    @Schema(description = "角色：NORMAL-普通成员 DEPARTMENT_HEAD-部门负责人 VICE_PRESIDENT-副社长 PRESIDENT-社长", example = "NORMAL")
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 30)
    private MemberRoleEnum role = MemberRoleEnum.NORMAL;

    @Schema(description = "是否活跃：0-不活跃 1-活跃", example = "1")
    @Column(name = "active", nullable = false)
    private Integer active = 1;

    @Schema(description = "参与活动次数", example = "15")
    @Column(name = "activity_count", nullable = false)
    private Integer activityCount = 0;

    @Schema(description = "加入时间", example = "2023-09-01 12:00:00")
    @Column(name = "join_time", length = 50)
    private String joinTime;

    @Schema(description = "个人简介", example = "热爱编程，擅长Java开发")
    @Column(name = "bio", length = 500)
    private String bio;
}
