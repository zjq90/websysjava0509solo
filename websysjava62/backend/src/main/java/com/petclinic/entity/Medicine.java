package com.petclinic.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 药品实体类
 * 用于管理宠物药品信息
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "medicine")
@Schema(description = "药品")
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "药品ID")
    private Long id;

    @Column(nullable = false, length = 100)
    @Schema(description = "药品名称", required = true)
    private String medicineName;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Column(length = 100)
    @Schema(description = "分类名称")
    private String categoryName;

    @Column(length = 500)
    @Schema(description = "药品说明")
    private String description;

    @Column(length = 100)
    @Schema(description = "生产厂家")
    private String manufacturer;

    @Column(precision = 10, scale = 2)
    @Schema(description = "价格")
    private BigDecimal price;

    @Schema(description = "库存数量")
    private Integer stock;

    @Column(length = 100)
    @Schema(description = "适用宠物类型")
    private String petType;

    @Column(length = 500)
    @Schema(description = "用法用量")
    private String usageDosage;

    @Column(length = 500)
    @Schema(description = "注意事项")
    private String attention;

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
        if (stock == null) {
            stock = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
