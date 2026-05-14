package com.photostudio.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 * 用于管理影楼拍摄订单信息
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "orders")
@Schema(description = "订单信息")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "订单ID", example = "1")
    private Long id;

    @NotBlank(message = "订单编号不能为空")
    @Column(unique = true, nullable = false, length = 30)
    @Schema(description = "订单编号", example = "ORD20240515001")
    private String orderNo;

    @Column(length = 20)
    @Schema(description = "客户姓名", example = "李四")
    private String customerName;

    @Column(length = 50)
    @Schema(description = "客户电话", example = "13900139000")
    private String customerPhone;

    @Column(length = 200)
    @Schema(description = "收货地址", example = "北京市朝阳区XX路XX号")
    private String address;

    @Column(precision = 10, scale = 2)
    @Schema(description = "订单金额", example = "5999.00")
    private BigDecimal amount;

    @Column(nullable = false, length = 20)
    @Schema(description = "订单状态", example = "COMPLETED")
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.CONFIRMED;

    @Column
    @Schema(description = "拍摄日期")
    private LocalDateTime shootingDate;

    @Column
    @Schema(description = "完成日期")
    private LocalDateTime completionDate;

    @Column(length = 500)
    @Schema(description = "备注")
    private String remark;

    @Column
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column
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

    /**
     * 订单状态枚举
     */
    public enum OrderStatus {
        PENDING("待确认"),
        CONFIRMED("已确认"),
        SHOOTING("拍摄中"),
        EDITING("修片中"),
        PRODUCING("制作中"),
        DELIVERING("配送中"),
        COMPLETED("已完成"),
        CANCELLED("已取消");

        private final String description;

        OrderStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
