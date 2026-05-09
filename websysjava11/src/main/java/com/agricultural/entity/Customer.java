package com.agricultural.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 客户/供应商实体类
 * 统一管理客户（销售）和供应商（采购）信息，是利润分析的关键维度之一
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer")
public class Customer {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 客户/供应商编号
     */
    @Column(nullable = false, unique = true, length = 50)
    private String customerCode;

    /**
     * 客户/供应商名称
     */
    @Column(nullable = false, length = 150)
    private String customerName;

    /**
     * 客户类型：CUSTOMER（客户）、SUPPLIER（供应商）、BOTH（既是客户也是供应商）
     */
    @Column(nullable = false, length = 20)
    private String customerType;

    /**
     * 联系人
     */
    @Column(length = 50)
    private String contactPerson;

    /**
     * 联系电话
     */
    @Column(length = 30)
    private String contactPhone;

    /**
     * 省
     */
    @Column(length = 50)
    private String province;

    /**
     * 市
     */
    @Column(length = 50)
    private String city;

    /**
     * 区/县
     */
    @Column(length = 50)
    private String district;

    /**
     * 详细地址
     */
    @Column(length = 200)
    private String address;

    /**
     * 信用额度
     */
    @Column(precision = 15, scale = 2)
    private java.math.BigDecimal creditLimit;

    /**
     * 税号
     */
    @Column(length = 50)
    private String taxNumber;

    /**
     * 开户银行
     */
    @Column(length = 100)
    private String bankName;

    /**
     * 银行账号
     */
    @Column(length = 50)
    private String bankAccount;

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

    /**
     * 是否启用
     */
    @Column(nullable = false)
    private Boolean enabled = true;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
