package com.appsys.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 销售订单实体类
 * 管理销售订单的主信息，包括订单状态、金额、客户信息等
 * 支持完整的订单闭环流程：创建->确认->签署->ERP同步->备货->发货->签收->完成
 * 
 * @author appsys-team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sales_orders")
@Schema(description = "销售订�?)
public class SalesOrder {

    /**
     * 订单ID，自增主�?     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "订单ID", example = "1")
    private Long id;

    /**
     * 订单编号（业务主键，唯一�?     */
    @Column(nullable = false, unique = true, length = 20)
    @Schema(description = "订单编号", example = "SO202605100001")
    private String orderNo;

    /**
     * 关联客户ID
     */
    @Column(nullable = false)
    @Schema(description = "客户ID", example = "1")
    private Long customerId;

    /**
     * 订单状�?     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Schema(description = "订单状�?, example = "PENDING_CONFIRMATION")
    private OrderStatus status = OrderStatus.PENDING_CONFIRMATION;

    /**
     * 订单原价总金�?     */
    @Column(precision = 12, scale = 2, nullable = false)
    @Schema(description = "订单原价总金�?, example = "1000.00")
    private BigDecimal originalAmount = BigDecimal.ZERO;

    /**
     * 折扣金额
     */
    @Column(precision = 12, scale = 2, nullable = false)
    @Schema(description = "折扣金额", example = "100.00")
    private BigDecimal discountAmount = BigDecimal.ZERO;

    /**
     * 实际支付金额
     */
    @Column(precision = 12, scale = 2, nullable = false)
    @Schema(description = "实际支付金额", example = "900.00")
    private BigDecimal actualAmount = BigDecimal.ZERO;

    /**
     * 应用的折扣率（根据客户等级）
     */
    @Column(precision = 4, scale = 2)
    @Schema(description = "应用的折扣率", example = "0.90")
    private BigDecimal appliedDiscountRate;

    /**
     * 客户等级（下单时的等级快照）
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Schema(description = "客户等级快照", example = "VIP")
    private CustomerLevel customerLevelAtOrder;

    /**
     * 业务员ID
     */
    @Schema(description = "业务员ID", example = "1")
    private Long salespersonId;

    /**
     * 业务员姓�?     */
    @Column(length = 50)
    @Schema(description = "业务员姓�?, example = "李四")
    private String salespersonName;

    /**
     * 收货地址
     */
    @Column(length = 300)
    @Schema(description = "收货地址", example = "北京市朝阳区XX街道XX�?)
    private String shippingAddress;

    /**
     * 收货人手机号
     */
    @Column(length = 11)
    @Schema(description = "收货人手机号", example = "13800138000")
    private String receiverPhone;

    /**
     * 收货人姓�?     */
    @Column(length = 50)
    @Schema(description = "收货人姓�?, example = "张三")
    private String receiverName;

    /**
     * 备注
     */
    @Column(columnDefinition = "TEXT")
    @Schema(description = "订单备注", example = "请尽快发�?)
    private String remarks;

    /**
     * 合同编号（签署后生成�?     */
    @Column(unique = true, length = 30)
    @Schema(description = "合同编号", example = "CT202605100001")
    private String contractNo;

    /**
     * 合同签署时间
     */
    @Schema(description = "合同签署时间")
    private LocalDateTime signedAt;

    /**
     * 同步到ERP的时�?     */
    @Schema(description = "ERP同步时间")
    private LocalDateTime erpSyncedAt;

    /**
     * 发货时间
     */
    @Schema(description = "发货时间")
    private LocalDateTime shippedAt;

    /**
     * 签收时间
     */
    @Schema(description = "签收时间")
    private LocalDateTime deliveredAt;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    @Schema(description = "创建时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;

    /**
     * 订单详情列表
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "订单详情列表")
    private List<OrderItem> items = new ArrayList<>();

    /**
     * 客户名称（非持久化，用于前端展示�?     */
    @Transient
    @Schema(description = "客户名称", example = "张三")
    private String customerName;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * 兼容前端getTotalAmount（返回实际支付金额）
     * @return 实际支付金额
     */
    @Transient
    @Schema(hidden = true)
    public BigDecimal getTotalAmount() {
        return this.actualAmount;
    }

    /**
     * 兼容前端getTotalItems（统计商品总件数）
     * @return 商品总件�?     */
    @Transient
    @Schema(hidden = true)
    public Integer getTotalItems() {
        if (this.items == null) return 0;
        return this.items.stream().mapToInt(OrderItem::getQuantity).sum();
    }

    /**
     * 兼容前端getCreateTime
     * @return 创建时间
     */
    @Transient
    @Schema(hidden = true)
    public LocalDateTime getCreateTime() {
        return this.createdAt;
    }

    /**
     * 兼容前端getContractSignedTime
     * @return 合同签署时间
     */
    @Transient
    @Schema(hidden = true)
    public LocalDateTime getContractSignedTime() {
        return this.signedAt;
    }

    /**
     * 兼容前端getErpSyncTime
     * @return ERP同步时间
     */
    @Transient
    @Schema(hidden = true)
    public LocalDateTime getErpSyncTime() {
        return this.erpSyncedAt;
    }

    /**
     * 兼容前端getStockPrepareTime
     * 当状态为备货中或之后时，返回ERP同步时间作为备货开始时�?     * @return 备货开始时�?     */
    @Transient
    @Schema(hidden = true)
    public LocalDateTime getStockPrepareTime() {
        if (this.status == OrderStatus.STOCK_PREPARING || 
            this.status == OrderStatus.SHIPPED ||
            this.status == OrderStatus.DELIVERED ||
            this.status == OrderStatus.COMPLETED) {
            return this.erpSyncedAt;
        }
        return null;
    }

    /**
     * 兼容前端getShippedTime
     * @return 发货时间
     */
    @Transient
    @Schema(hidden = true)
    public LocalDateTime getShippedTime() {
        return this.shippedAt;
    }

    /**
     * 兼容前端getOrderItems
     * @return 订单明细列表
     */
    @Transient
    @Schema(hidden = true)
    public List<OrderItem> getOrderItems() {
        return this.items;
    }
}
