package com.club.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 用户实体类
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "t_user")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户信息")
public class User extends BaseEntity {

    @Schema(description = "用户名", example = "student001")
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Schema(description = "密码", example = "123456")
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Schema(description = "真实姓名", example = "张三")
    @Column(name = "real_name", nullable = false, length = 50)
    private String realName;

    @Schema(description = "学号", example = "2021001001")
    @Column(name = "student_no", length = 50)
    private String studentNo;

    @Schema(description = "性别：1-男 2-女", example = "1")
    @Column(name = "gender")
    private Integer gender;

    @Schema(description = "头像", example = "https://example.com/avatar.png")
    @Column(name = "avatar", length = 500)
    private String avatar;

    @Schema(description = "手机号", example = "13800138000")
    @Column(name = "phone", length = 20)
    private String phone;

    @Schema(description = "邮箱", example = "student@example.com")
    @Column(name = "email", length = 100)
    private String email;

    @Schema(description = "院系", example = "计算机科学与技术学院")
    @Column(name = "college", length = 100)
    private String college;

    @Schema(description = "专业", example = "软件工程")
    @Column(name = "major", length = 100)
    private String major;

    @Schema(description = "年级", example = "2021级")
    @Column(name = "grade", length = 50)
    private String grade;

    @Schema(description = "个人简介", example = "热爱编程，喜欢参加技术交流活动")
    @Column(name = "bio", length = 500)
    private String bio;

    @Schema(description = "学校ID", example = "1")
    @Column(name = "school_id", nullable = false)
    private Long schoolId;

    @Schema(description = "角色：ADMIN-管理员 USER-普通用户", example = "USER")
    @Column(name = "role", nullable = false, length = 20)
    private String role = "USER";

    @Schema(description = "状态：0-禁用 1-启用", example = "1")
    @Column(name = "status", nullable = false)
    private Integer status = 1;
}
