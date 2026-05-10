package com.websys.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色实体类
 * 定义系统中的用户角色，如管理员、运营、维护、财务等
 */
@Entity
@Table(name = "sys_role")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "角色")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "角色ID")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @Schema(description = "角色名称")
    private String name;

    @Column(nullable = false, unique = true, length = 50)
    @Schema(description = "角色编码")
    private String code;

    @Column(length = 200)
    @Schema(description = "角色描述")
    private String description;

    @Column(nullable = false)
    @Schema(description = "状态：1启用，0禁用")
    private Integer status = 1;

    @Column(name = "create_time", nullable = false, updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime = LocalDateTime.now();

    @Column(name = "update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sys_role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    @Schema(description = "角色权限列表")
    private List<Permission> permissions;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sys_role_menu",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_id")
    )
    @Schema(description = "角色菜单列表")
    private List<Menu> menus;
}
