package com.seedtrace.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 销售信息实体类
 * 
 * <p>存储种子销售和客户信息。</p>
 * 
 * <p>注意：敏感字段使用AES-256加密存储：
 * <ul>
 *   <li>客户手机号</li>
 *   <li>客户身份证号</li>
 * </ul>
 * </p>
 * 
 * <p>数据校验规则：
 * <ul>
 *   <li>客户手机号：符合中国大陆手机号格式（1开头，11位）</li>
 * </ul>
 * </p>
 * 
 * @author Seed Trace System
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sales_info")
public class SalesInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 批次ID（外键）
     */
    @Column(name = "batch_id", nullable = false)
    private Long batchId;

    /**
     * 销售日期
     */
    @NotNull(message = "销售日期不能为空")
    @Column(nullable = false)
    private LocalDate salesDate;

    /**
     * 客户名称
     */
    @NotBlank(message = "客户名称不能为空")
    @Column(nullable = false, length = 100)
    private String customerName;

    /**
     * 客户手机号（加密存储）
     * 校验规则：符合中国大陆手机号格式（1开头，11位）
     */
    @NotBlank(message = "客户手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "客户手机号格式不正确，必须是1开头的11位数字")
    @Column(nullable = false, length = 200)
    private String customerPhone;

    /**
     * 客户地址
     */
    @Column(length = 500)
    private String customerAddress;

    /**
     * 客户身份证号（加密存储）
     */
    @Column(length = 200)
    private String customerIdCard;

    /**
     * 购买数量(kg)
     */
    @NotNull(message = "购买数量不能为空")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal purchaseQuantity;

    /**
     * 单价(元/kg)
     */
    @NotNull(message = "单价不能为空")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    /**
     * 总金额
     */
    @NotNull(message = "总金额不能为空")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    /**
     * 销售人员
     */
    @Column(length = 100)
    private String salesPerson;

    /**
     * 销售渠道
     */
    @Column(length = 50)
    private String salesChannel;

    /**
     * 配送地址
     */
    @Column(length = 500)
    private String deliveryAddress;

    /**
     * 备注
     */
    @Column(length = 500)
    private String remark;

    /**
     * 创建时间
     */
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
