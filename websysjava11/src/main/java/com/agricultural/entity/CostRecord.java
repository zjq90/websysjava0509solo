package com.agricultural.entity;

import com.agricultural.entity.enums.CostCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 成本记录实体类
 * 多角度核算成本：育种成本、田间投入、加工费用、包装物流等
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cost_record")
public class CostRecord {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 成本单号
     */
    @Column(nullable = false, unique = true, length = 50)
    private String costNo;

    /**
     * 成本类别
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CostCategory category;

    /**
     * 关联的品种（可选，按品种核算）
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variety_id")
    private Variety variety;

    /**
     * 费用名称
     */
    @Column(nullable = false, length = 150)
    private String expenseName;

    /**
     * 费用日期
     */
    @Column(nullable = false)
    private LocalDate expenseDate;

    /**
     * 数量
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal quantity;

    /**
     * 单位
     */
    @Column(length = 20)
    private String unit;

    /**
     * 单价
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal unitPrice;

    /**
     * 总金额
     */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    /**
     * 供应商（可选）
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Customer supplier;

    /**
     * 批次号/作业批次
     */
    @Column(length = 50)
    private String batchNo;

    /**
     * 区域/地块
     */
    @Column(length = 100)
    private String area;

    /**
     * 经办人
     */
    @Column(length = 50)
    private String handler;

    /**
     * 备注
     */
    @Column(length = 500)
    private String remark;

    /**
     * 创建时间
     */
    @Column(nullable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }

    /**
     * 计算总金额
     */
    public void calculateTotalAmount() {
        if (quantity != null && unitPrice != null) {
            this.totalAmount = quantity.multiply(unitPrice);
        }
    }
}
