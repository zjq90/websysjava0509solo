package com.referee.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.persistence.*;

/**
 * 用户实体类
 * 存储系统登录用户信息，包括管理员、裁判、裁判长、运动员
 *
 * @author Referee System
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "sys_user")
@Schema(description = "用户实体")
public class User {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "用户ID", example = "1")
    private Long id;

    /**
     * 用户名（登录账号）
     */
    @Column(nullable = false, unique = true, length = 50)
    @Schema(description = "用户名", example = "admin", required = true)
    private String username;

    /**
     * 密码
     */
    @Column(nullable = false, length = 100)
    @Schema(description = "密码", example = "123456", required = true)
    private String password;

    /**
     * 真实姓名
     */
    @Column(length = 50)
    @Schema(description = "真实姓名", example = "张三")
    private String realName;

    /**
     * 用户角色：ADMIN-管理员, CHIEF_REFEREE-裁判长, REFEREE-裁判, ATHLETE-运动员
     */
    @Column(nullable = false, length = 20)
    @Schema(description = "用户角色", example = "REFEREE", required = true)
    private String role;

    /**
     * 手机号
     */
    @Column(length = 20)
    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    /**
     * 邮箱
     */
    @Column(length = 100)
    @Schema(description = "邮箱", example = "user@example.com")
    private String email;

    /**
     * 状态：1-启用, 0-禁用
     */
    @Column(nullable = false)
    @Schema(description = "状态：1-启用, 0-禁用", example = "1")
    private Integer status = 1;

    /**
     * 关联的裁判ID（如果是裁判角色）
     */
    @Column
    @Schema(description = "关联裁判ID", example = "1")
    private Long refereeId;

    /**
     * 关联的运动员ID（如果是运动员角色）
     */
    @Column
    @Schema(description = "关联运动员ID", example = "1")
    private Long athleteId;
}
