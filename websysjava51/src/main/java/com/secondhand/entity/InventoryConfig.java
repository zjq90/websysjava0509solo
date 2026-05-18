package com.secondhand.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_inventory_config")
@Schema(description = "库存预警配置实体")
public class InventoryConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "配置ID")
    private Long id;

    @Schema(description = "商品分类")
    @Column(length = 100)
    private String category;

    @Schema(description = "预警阈值")
    @Column(nullable = false)
    private Integer warningThreshold;

    @Schema(description = "是否启用")
    @Column(nullable = false)
    private Boolean enabled;

    @Schema(description = "创建时间")
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @Column(nullable = false)
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (enabled == null) {
            enabled = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }

}