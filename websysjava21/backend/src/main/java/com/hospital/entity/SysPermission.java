package com.hospital.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 系统权限实体类
 * 
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "sys_permission")
@Schema(description = "系统权限")
public class SysPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "权限ID", example = "1")
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    @Schema(description = "权限编码", example = "system:user:list")
    private String permissionCode;

    @Column(nullable = false, length = 50)
    @Schema(description = "权限名称", example = "用户列表")
    private String permissionName;

    @Column(length = 200)
    @Schema(description = "权限描述")
    private String description;

    @Column(length = 50)
    @Schema(description = "资源类型：menu-菜单，button-按钮", example = "menu")
    private String resourceType;

    @Column
    @Schema(description = "父权限ID", example = "0")
    private Long parentId;

    @Column
    @Schema(description = "排序号", example = "1")
    private Integer sortOrder;

    @Column(nullable = false)
    @Schema(description = "状态：0-禁用，1-启用", example = "1")
    private Integer status = 1;

    @Column(name = "create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @ManyToMany(mappedBy = "permissions")
    @Schema(description = "拥有该权限的角色列表")
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
