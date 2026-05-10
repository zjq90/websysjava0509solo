package com.appsys.system.entity;

import com.appsys.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 系统权限实体类
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_permission", indexes = {
    @Index(name = "idx_permission_code", columnList = "permission_code", unique = true)
})
@Schema(description = "系统权限")
public class SysPermission extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "权限编码")
    @Column(name = "permission_code", nullable = false, length = 100, unique = true)
    private String permissionCode;

    @Schema(description = "权限名称")
    @Column(name = "permission_name", nullable = false, length = 50)
    private String permissionName;

    @Schema(description = "权限类型：menu-菜单，button-按钮，api-接口")
    @Column(name = "permission_type", length = 20)
    private String permissionType;

    @Schema(description = "父级权限ID")
    @Column(name = "parent_id")
    private Long parentId;

    @Schema(description = "排序号")
    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    @Schema(description = "前端路由路径")
    @Column(name = "path", length = 255)
    private String path;

    @Schema(description = "后端接口URL")
    @Column(name = "url", length = 255)
    private String url;

    @Schema(description = "请求方法")
    @Column(name = "method", length = 10)
    private String method;

    @Schema(description = "图标")
    @Column(name = "icon", length = 50)
    private String icon;

    @Schema(description = "描述")
    @Column(name = "description", length = 255)
    private String description;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private Set<SysRole> roles = new HashSet<>();
}
