package com.photostudio.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 套餐实体类
 * 存储影楼提供的摄影套餐信息
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "ps_package")
@Schema(description = "套餐信息")
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "套餐ID")
    private Long id;

    @Column(nullable = false, length = 100)
    @Schema(description = "套餐名称")
    private String name;

    /**
     * 套餐类型：
     * WEDDING-婚纱照
     * PORTRAIT-个人写真
     * FAMILY-全家福
     * CHILDREN-儿童摄影
     * COMMERCIAL-商业摄影
     */
    @Column(nullable = false, length = 30)
    @Schema(description = "套餐类型")
    private String type;

    @Column(length = 500)
    @Schema(description = "套餐描述")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    @Schema(description = "套餐价格")
    private BigDecimal price;

    @Column(name = "cost_labor", precision = 10, scale = 2)
    @Schema(description = "人工成本")
    private BigDecimal costLabor = BigDecimal.ZERO;

    @Column(name = "cost_clothing", precision = 10, scale = 2)
    @Schema(description = "服装成本")
    private BigDecimal costClothing = BigDecimal.ZERO;

    @Column(name = "cost_materials", precision = 10, scale = 2)
    @Schema(description = "耗材成本")
    private BigDecimal costMaterials = BigDecimal.ZERO;

    @Column(name = "total_orders")
    @Schema(description = "总订单数")
    private Integer totalOrders = 0;

    @Column(name = "total_revenue", precision = 12, scale = 2)
    @Schema(description = "总营收")
    private BigDecimal totalRevenue = BigDecimal.ZERO;

    @Column(name = "is_active")
    @Schema(description = "是否启用")
    private Boolean active = true;

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
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
