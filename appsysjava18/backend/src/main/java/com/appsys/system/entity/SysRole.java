package com.appsys.system.entity;

import com.appsys.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 系统角色实体类
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_role", indexes = {
    @Index(name = "idx_role_code", columnList = "role_code", unique = true)
})
@Schema(description = "系统角色")
public class SysRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "角色编码")
    @Column(name = "role_code", nullable = false, length = 50, unique = true)
    private String roleCode;

    @Schema(description = "角色名称")
    @Column(name = "role_name", nullable = false, length = 50)
    private String roleName;

    @Schema(description = "角色描述")
    @Column(name = "description", length = 255)
    private String description;

    @Schema(description = "状态：1-启用，0-禁用")
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<SysUser> users = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "sys_role_permission",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<SysPermission> permissions = new HashSet<>();
}
