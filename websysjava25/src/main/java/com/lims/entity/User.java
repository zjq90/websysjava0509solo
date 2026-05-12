package com.lims.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 存储系统用户信息，包括医生、技师、审核人员等
 *
 * @author LIMS Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_user")
@Schema(description = "用户信息")
public class User {

    /**
     * 用户ID，主键自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "用户ID", example = "1")
    private Long id;

    /**
     * 用户账号，唯一标识
     */
    @Column(unique = true, nullable = false, length = 50)
    @Schema(description = "用户账号", example = "admin")
    private String username;

    /**
     * 用户密码
     */
    @Column(nullable = false, length = 100)
    @Schema(description = "用户密码", example = "123456")
    private String password;

    /**
     * 用户真实姓名
     */
    @Column(nullable = false, length = 50)
    @Schema(description = "真实姓名", example = "张三")
    private String realName;

    /**
     * 所属科室ID
     */
    @Column
    @Schema(description = "所属科室ID", example = "1")
    private Long departmentId;

    /**
     * 用户角色：ADMIN-管理员，DOCTOR-临床医生，TECHNICIAN-技师，AUDITOR-审核人员，PATIENT-患者
     */
    @Column(nullable = false, length = 50)
    @Schema(description = "用户角色", example = "TECHNICIAN")
    private String role;

    /**
     * 联系电话
     */
    @Column(length = 20)
    @Schema(description = "联系电话", example = "13800138000")
    private String phone;

    /**
     * 电子邮箱
     */
    @Column(length = 100)
    @Schema(description = "电子邮箱", example = "user@example.com")
    private String email;

    /**
     * 状态：ACTIVE-启用，INACTIVE-停用
     */
    @Column(nullable = false, length = 20)
    @Schema(description = "状态", example = "ACTIVE")
    private String status;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        status = "ACTIVE";
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
