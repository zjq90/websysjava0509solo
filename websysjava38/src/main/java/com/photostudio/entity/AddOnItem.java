package com.photostudio.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 加购项实体类
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "add_on_item")
@Schema(description = "加购项信息")
public class AddOnItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "加购项ID", example = "1")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    @Schema(description = "加购项名称", example = "加拍夜景")
    private String name;

    @Column(name = "category", length = 50)
    @Schema(description = "类别", example = "拍摄/精修/产品/其他")
    private String category;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    @Schema(description = "价格", example = "500.00")
    private BigDecimal price;

    @Column(name = "description", length = 500)
    @Schema(description = "描述", example = "额外增加2小时夜景拍摄")
    private String description;

    @Column(name = "unit", length = 20)
    @Schema(description = "单位", example = "次/张/个/套")
    private String unit;

    @Column(name = "status")
    @Schema(description = "状态 0-不可用 1-可用", example = "1")
    private Integer status;

    @Column(name = "sort_order")
    @Schema(description = "排序", example = "1")
    private Integer sortOrder;

    @Column(name = "create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (status == null) {
            status = 1;
        }
        if (sortOrder == null) {
            sortOrder = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
