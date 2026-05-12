package com.hospital.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 系统角色实体类
 * 
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "sys_role")
@Schema(description = "系统角色")
public class SysRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "角色ID", example = "1")
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    @Schema(description = "角色编码", example = "ADMIN")
    private String roleCode;

    @Column(nullable = false, length = 50)
    @Schema(description = "角色名称", example = "系统管理员")
    private String roleName;

    @Column(length = 200)
    @Schema(description = "角色描述", example = "拥有系统所有权限")
    private String description;

    @Column(nullable = false)
    @Schema(description = "状态：0-禁用，1-启用", example = "1")
    private Integer status = 1;

    @Column(name = "create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @ManyToMany(mappedBy = "roles")
    @Schema(description = "拥有该角色的用户列表")
    private Set<SysUser> users = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "sys_role_permission",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    @Schema(description = "角色权限列表")
    private Set<SysPermission> permissions = new HashSet<>();

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
