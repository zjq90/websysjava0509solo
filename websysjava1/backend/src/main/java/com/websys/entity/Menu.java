package com.websys.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 菜单实体类
 * 定义系统菜单结构，支持多级菜单
 */
@Entity
@Table(name = "sys_menu")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "菜单")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "菜单ID")
    private Long id;

    @Column(nullable = false, length = 50)
    @Schema(description = "菜单名称")
    private String name;

    @Column(name = "parent_id")
    @Schema(description = "父菜单ID")
    private Long parentId;

    @Column(length = 100)
    @Schema(description = "菜单路径")
    private String path;

    @Column(length = 100)
    @Schema(description = "组件路径")
    private String component;

    @Column(length = 50)
    @Schema(description = "菜单图标")
    private String icon;

    @Column(nullable = false)
    @Schema(description = "排序号")
    private Integer sortOrder = 0;

    @Column(nullable = false)
    @Schema(description = "菜单类型：1目录，2菜单，3按钮")
    private Integer type = 1;

    @Column(nullable = false)
    @Schema(description = "状态：1显示，0隐藏")
    private Integer status = 1;

    @Column(name = "create_time", nullable = false, updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime = LocalDateTime.now();

    @Column(name = "update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
