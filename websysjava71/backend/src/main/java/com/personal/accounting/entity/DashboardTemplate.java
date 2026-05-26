package com.personal.accounting.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 看板模板实体类
 * 用于保存用户自定义的看板布局
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@Entity
@Table(name = "dashboard_templates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "看板模板实体")
public class DashboardTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "模板ID", example = "1")
    private Long id;

    @Column(nullable = false, length = 100)
    @Schema(description = "模板名称", example = "月度复盘模板")
    private String name;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "模板描述", example = "用于每月财务复盘的看板布局")
    private String description;

    @Column(columnDefinition = "TEXT", nullable = false)
    @Schema(description = "看板布局配置（JSON格式）")
    private String layoutConfig;

    @Column
    @Schema(description = "是否为默认模板", example = "false")
    private Boolean isDefault = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
