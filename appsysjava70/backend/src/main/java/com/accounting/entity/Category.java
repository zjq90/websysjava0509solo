package com.accounting.entity;

import com.accounting.enums.BillType;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")
@Schema(description = "分类实体")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "分类ID", example = "1")
    private Long id;

    @Column(nullable = false, length = 50)
    @Schema(description = "分类名称", example = "餐饮")
    private String name;

    @Column(length = 200)
    @Schema(description = "分类图标", example = "🍔")
    private String icon;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "账单类型")
    private BillType type;

    @Column(length = 50)
    @Schema(description = "父分类名称", example = "食品饮料")
    private String parentName;

    @Column(name = "sort_order")
    @Schema(description = "排序", example = "1")
    private Integer sortOrder;

    @CreationTimestamp
    @Column(updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}
