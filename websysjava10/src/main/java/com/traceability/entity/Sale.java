package com.traceability.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 销售实体类
 * 记录销售环节信息，追溯链条的第六个环节
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sale")
public class Sale extends BaseEntity {

    @Column(name = "sale_no", nullable = false, unique = true, length = 50)
    private String saleNo;

    @Column(name = "batch_no", length = 50)
    private String batchNo;

    @Column(name = "package_no", length = 50)
    private String packageNo;

    @Column(name = "sale_date", length = 20)
    private String saleDate;

    @Column(name = "customer_name", length = 100)
    private String customerName;

    @Column(name = "customer_phone", length = 20)
    private String customerPhone;

    @Column(name = "customer_address", length = 200)
    private String customerAddress;

    @Column(name = "sale_quantity", precision = 10, scale = 2)
    private BigDecimal saleQuantity;

    @Column(name = "unit", length = 20)
    private String unit;

    @Column(name = "unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "total_amount", precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "salesperson", length = 50)
    private String salesperson;

    @Column(name = "logistics_info", length = 200)
    private String logisticsInfo;

    @Column(name = "delivery_status", length = 20)
    private String deliveryStatus;

    @Column(name = "remark", length = 500)
    private String remark;
}
