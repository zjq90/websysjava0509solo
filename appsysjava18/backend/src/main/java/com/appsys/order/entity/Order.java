package com.appsys.order.entity;

import com.appsys.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单实体类
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "orders", indexes = {
    @Index(name = "idx_order_no", columnList = "order_no", unique = true),
    @Index(name = "idx_customer_id", columnList = "customer_id"),
    @Index(name = "idx_deleted", columnList = "deleted")
})
@Schema(description = "订单信息")
public class Order extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "订单编号")
    @Column(name = "order_no", nullable = false, length = 50, unique = true)
    private String orderNo;

    @Schema(description = "客户ID")
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Schema(description = "客户名称（冗余字段）")
    @Column(name = "customer_name", length = 100)
    private String customerName;

    @Schema(description = "订单日期")
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Schema(description = "订单金额")
    @Column(name = "order_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal orderAmount;

    @Schema(description = "优惠金额")
    @Column(name = "discount_amount", precision = 12, scale = 2)
    private BigDecimal discountAmount;

    @Schema(description = "实付金额")
    @Column(name = "actual_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal actualAmount;

    @Schema(description = "订单状态：1-待发货，2-已发货，3-已完成，4-已取消")
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    @Schema(description = "销售员ID")
    @Column(name = "salesman_id")
    private Long salesmanId;

    @Schema(description = "销售员名称")
    @Column(name = "salesman_name", length = 50)
    private String salesmanName;

    @Schema(description = "备注")
    @Column(name = "remark", length = 500)
    private String remark;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    private Customer customer;
}
