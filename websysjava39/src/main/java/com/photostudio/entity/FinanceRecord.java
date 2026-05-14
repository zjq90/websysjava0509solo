package com.photostudio.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 收支记录实体类
 * 存储影楼所有的收入和支出记录
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "ps_finance_record")
@Schema(description = "收支记录")
public class FinanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "记录ID")
    private Long id;

    @Column(nullable = false, length = 50)
    @Schema(description = "记录编号")
    private String recordNo;

    /**
     * 类型：INCOME-收入，EXPENSE-支出
     */
    @Column(nullable = false, length = 20)
    @Schema(description = "类型")
    private String type;

    /**
     * 分类：
     * 收入：ORDER_PAYMENT-订单收款，DEPOSIT-定金，BALANCE-尾款
     * 支出：SALARY-工资，RENT-房租，MATERIALS-耗材，EQUIPMENT-设备，OTHER-其他
     */
    @Column(nullable = false, length = 30)
    @Schema(description = "分类")
    private String category;

    @Column(nullable = false, precision = 12, scale = 2)
    @Schema(description = "金额")
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    @Schema(description = "所属门店")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @Schema(description = "关联订单")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    @Schema(description = "关联员工")
    private Employee employee;

    @Column(length = 200)
    @Schema(description = "备注")
    private String remark;

    @Column(name = "record_date", nullable = false)
    @Schema(description = "记录日期")
    private LocalDate recordDate;

    @Column(name = "create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
