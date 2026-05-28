package com.club.management.entity;

import com.club.management.entity.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户实体类
 * 
 * @author club-management
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_user")
public class User extends BaseEntity {

    /**
     * 学号
     */
    @Column(name = "student_no", unique = true, nullable = false, length = 20)
    private String studentNo;

    /**
     * 用户名
     */
    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    /**
     * 密码
     */
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    /**
     * 真实姓名
     */
    @Column(name = "real_name", nullable = false, length = 50)
    private String realName;

    /**
     * 性别
     */
    @Column(name = "gender", length = 10)
    private String gender;

    /**
     * 手机号码
     */
    @Column(name = "phone", length = 20)
    private String phone;

    /**
     * 邮箱
     */
    @Column(name = "email", length = 100)
    private String email;

    /**
     * 院系
     */
    @Column(name = "department", length = 100)
    private String department;

    /**
     * 专业
     */
    @Column(name = "major", length = 100)
    private String major;

    /**
     * 班级
     */
    @Column(name = "class_name", length = 50)
    private String className;

    /**
     * 年级
     */
    @Column(name = "grade", length = 10)
    private String grade;

    /**
     * 头像URL
     */
    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    /**
     * 用户角色
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private UserRole role = UserRole.STUDENT;

    /**
     * 是否启用
     */
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;
}
