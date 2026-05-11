package com.hospital.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户实体类
 * 系统用户（患者、医生、管理员等）
 * 
 * @author hospital
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "sys_user")
@Schema(description = "用户信息")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "用户ID")
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    @Schema(description = "用户名")
    private String username;

    @Column(nullable = false, length = 100)
    @Schema(description = "密码（加密存储）")
    private String password;

    @Column(length = 50)
    @Schema(description = "真实姓名")
    private String realName;

    @Column(length = 50)
    @Schema(description = "身份证号（加密存储）")
    private String idCard;

    @Column(length = 20)
    @Schema(description = "手机号（加密存储）")
    private String phone;

    @Column(length = 10)
    @Schema(description = "性别")
    private String gender;

    @Column
    @Schema(description = "年龄")
    private Integer age;

    @Column(length = 200)
    @Schema(description = "地址")
    private String address;

    @Column(length = 100)
    @Schema(description = "邮箱")
    private String email;

    @Column(length = 200)
    @Schema(description = "头像URL")
    private String avatar;

    @Column(nullable = false)
    @Schema(description = "状态：0禁用 1启用")
    private Integer status = 1;

    @Column(length = 20)
    @Schema(description = "患者编号")
    private String patientNo;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "sys_user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Schema(description = "用户角色列表")
    private Set<Role> roles = new HashSet<>();

    @Column(nullable = false, updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Column(length = 50)
    @Schema(description = "创建人")
    private String createBy;

    @Column(length = 50)
    @Schema(description = "更新人")
    private String updateBy;

    @Column
    @Schema(description = "长辈模式：0关闭 1开启")
    private Integer elderMode = 0;

    @Column
    @Schema(description = "最后登录时间")
    private LocalDateTime lastLoginTime;

    @Column(length = 50)
    @Schema(description = "最后登录IP")
    private String lastLoginIp;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}
