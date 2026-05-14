package com.photostudio.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 员工实体类
 * 存储影楼员工的基本信息，包括摄影师、化妆师、选片师、修图师等
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "ps_employee")
@Schema(description = "员工信息")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "员工ID")
    private Long id;

    @Column(nullable = false, length = 50)
    @Schema(description = "员工姓名")
    private String name;

    @Column(length = 20)
    @Schema(description = "联系电话")
    private String phone;

    /**
     * 岗位类型：
     * SALES-销售员
     * PHOTOGRAPHER-摄影师
     * MAKEUP_ARTIST-化妆师
     * PHOTO_SELECTOR-选片师
     * PHOTO_EDITOR-修图师
     * MANAGER-管理人员
     */
    @Column(nullable = false, length = 30)
    @Schema(description = "岗位类型")
    private String position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    @Schema(description = "所属门店")
    private Store store;

    @Column(name = "hire_date")
    @Schema(description = "入职日期")
    private LocalDate hireDate;

    @Column(precision = 10, scale = 2)
    @Schema(description = "基础工资")
    private BigDecimal baseSalary;

    @Column(name = "average_rating", precision = 3, scale = 2)
    @Schema(description = "平均评分")
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Column(name = "total_orders")
    @Schema(description = "总接单量")
    private Integer totalOrders = 0;

    @Column(name = "total_revenue", precision = 12, scale = 2)
    @Schema(description = "总营收")
    private BigDecimal totalRevenue = BigDecimal.ZERO;

    @Column(name = "is_active")
    @Schema(description = "是否在职")
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
