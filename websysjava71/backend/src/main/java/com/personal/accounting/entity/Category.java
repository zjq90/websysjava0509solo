package com.personal.accounting.entity;

import com.personal.accounting.entity.enums.CategoryType;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 分类实体类
 * 用于管理收入和支出的分类
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分类实体")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "分类ID", example = "1")
    private Long id;

    @Column(nullable = false, length = 50)
    @Schema(description = "分类名称", example = "餐饮")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "分类类型", example = "EXPENSE")
    private CategoryType type;

    @Column(length = 7)
    @Schema(description = "图标颜色", example = "#FF6B6B")
    private String color;

    @Column(length = 50)
    @Schema(description = "图标名称", example = "food")
    private String icon;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "分类描述", example = "餐饮相关支出")
    private String description;

    @Column(nullable = false)
    @Schema(description = "排序", example = "1")
    private Integer sortOrder = 0;

    @Column(nullable = false)
    @Schema(description = "是否启用", example = "true")
    private Boolean enabled = true;

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
