package com.broadband.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 账单实体类
 * 管理用户月度账单
 * 
 * @author broadband
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "biz_bill")
@Schema(description = "账单信息")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "账单ID")
    private Long id;

    @Schema(description = "账单编号")
    @Column(length = 50, nullable = false, unique = true)
    private String billNo;

    @Schema(description = "用户ID")
    @Column(nullable = false)
    private Long userId;

    @Schema(description = "用户套餐ID")
    private Long userPackageId;

    @Schema(description = "宽带号码")
    @Column(length = 20)
    private String broadbandNumber;

    @Schema(description = "账单周期(年-月)")
    @Column(length = 10, nullable = false)
    private String billPeriod;

    @Schema(description = "账单开始日期")
    @Column(nullable = false)
    private LocalDate startDate;

    @Schema(description = "账单结束日期")
    @Column(nullable = false)
    private LocalDate endDate;

    @Schema(description = "套餐费用")
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal packageFee;

    @Schema(description = "增值服务费用")
    @Column(precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0")
    private BigDecimal extraFee = BigDecimal.ZERO;

    @Schema(description = "其他费用")
    @Column(precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0")
    private BigDecimal otherFee = BigDecimal.ZERO;

    @Schema(description = "优惠金额")
    @Column(precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0")
    private BigDecimal discountFee = BigDecimal.ZERO;

    @Schema(description = "账单总金额")
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Schema(description = "已支付金额")
    @Column(precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0")
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Schema(description = "待支付金额")
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal unpaidAmount;

    @Schema(description = "缴费截止日期")
    @Column(nullable = false)
    private LocalDate dueDate;

    @Schema(description = "是否发送欠费提醒: 0-否 1-是")
    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer reminderSent = 0;

    @Schema(description = "欠费提醒发送时间")
    private LocalDateTime reminderTime;

    @Schema(description = "账单状态: 0-待支付 1-已支付 2-已逾期 3-已取消")
    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer status = 0;

    @Schema(description = "支付时间")
    private LocalDateTime payTime;

    @Schema(description = "账单明细(JSON)")
    @Column(length = 2000)
    private String details;

    @Schema(description = "备注")
    @Column(length = 500)
    private String remark;

    @Schema(description = "创建时间")
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

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
