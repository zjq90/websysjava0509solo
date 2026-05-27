package com.club.entity;

import com.club.enums.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 用户实体类
 * 存储系统用户信息，包括校级管理员和社团负责人
 *
 * @author Club Management System
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_user")
@Schema(description = "用户信息")
public class User extends BaseEntity {

    /**
     * 用户名
     */
    @Column(name = "username", nullable = false, unique = true, length = 50)
    @Schema(description = "用户名", example = "admin")
    private String username;

    /**
     * 密码（加密存储）
     */
    @Column(name = "password", nullable = false, length = 100)
    @Schema(description = "密码", example = "123456")
    private String password;

    /**
     * 真实姓名
     */
    @Column(name = "real_name", nullable = false, length = 50)
    @Schema(description = "真实姓名", example = "张三")
    private String realName;

    /**
     * 手机号
     */
    @Column(name = "phone", length = 20)
    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    /**
     * 邮箱
     */
    @Column(name = "email", length = 100)
    @Schema(description = "邮箱", example = "admin@club.edu.cn")
    private String email;

    /**
     * 所属院系
     */
    @Column(name = "department", length = 100)
    @Schema(description = "所属院系", example = "计算机学院")
    private String department;

    /**
     * 角色类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false, length = 30)
    @Schema(description = "角色类型", example = "SCHOOL_ADMIN")
    private RoleType roleType;

    /**
     * 负责社团ID
     */
    @Column(name = "club_id")
    @Schema(description = "负责社团ID", example = "1")
    private Long clubId;

    /**
     * 账号是否启用
     */
    @Column(name = "enabled", nullable = false)
    @Schema(description = "是否启用", example = "true")
    private Boolean enabled = true;

    /**
     * 备注
     */
    @Column(name = "remark", length = 500)
    @Schema(description = "备注", example = "系统管理员")
    private String remark;
}
