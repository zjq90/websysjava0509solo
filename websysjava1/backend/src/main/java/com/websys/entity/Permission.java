package com.websys.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 权限实体类
 * 定义系统中的操作权限，用于细粒度的权限控制
 */
@Entity
@Table(name = "sys_permission")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "权限")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "权限ID")
    private Long id;

    @Column(nullable = false, length = 50)
    @Schema(description = "权限名称")
    private String name;

    @Column(nullable = false, length = 100)
    @Schema(description = "权限编码")
    private String code;

    @Column(length = 200)
    @Schema(description = "权限描述")
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
}
