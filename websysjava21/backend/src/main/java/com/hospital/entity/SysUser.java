package com.hospital.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 系统用户实体类
 * 
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "sys_user")
@Schema(description = "系统用户")
public class SysUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "用户ID", example = "1")
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    @Schema(description = "用户名", example = "admin")
    private String username;

    @Column(nullable = false, length = 100)
    @JsonIgnore
    @Schema(description = "密码")
    private String password;

    @Column(length = 50)
    @Schema(description = "真实姓名", example = "张三")
    private String realName;

    @Column(length = 20)
    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    @Column(length = 100)
    @Schema(description = "邮箱", example = "admin@hospital.com")
    private String email;

    @Column(length = 200)
    @Schema(description = "头像URL")
    private String avatar;

    @Column(nullable = false)
    @Schema(description = "状态：0-禁用，1-启用", example = "1")
    private Integer status = 1;

    @Column(name = "create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "sys_user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Schema(description = "用户角色列表")
    private Set<SysRole> roles = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
