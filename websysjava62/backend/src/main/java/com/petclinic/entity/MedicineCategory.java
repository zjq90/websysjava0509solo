package com.petclinic.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 药品分类实体类
 * 用于管理药品的分类信息
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "medicine_category")
@Schema(description = "药品分类")
public class MedicineCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "分类ID")
    private Long id;

    @Column(nullable = false, length = 100)
    @Schema(description = "分类名称", required = true)
    private String categoryName;

    @Column(length = 500)
    @Schema(description = "分类描述")
    private String description;

    @Schema(description = "父分类ID")
    private Long parentId;

    @Schema(description = "排序号")
    private Integer sortOrder;

    @Column(length = 20)
    @Schema(description = "状态：ACTIVE-启用, INACTIVE-禁用")
    private String status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (status == null) {
            status = "ACTIVE";
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
