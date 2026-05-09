package com.sales.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 物流信息实体类
 * 存储订单的物流跟踪信息
 */
@Entity
@Table(name = "logistics")
public class Logistics {

    /**
     * 物流ID，主键，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 所属订单
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private SalesOrder order;

    /**
     * 物流单号
     */
    @Column(length = 50)
    private String trackingNo;

    /**
     * 物流公司
     */
    @Column(length = 50)
    private String logisticsCompany;

    /**
     * 发件地址
     */
    @Column(length = 200)
    private String fromAddress;

    /**
     * 收件地址
     */
    @Column(length = 200)
    private String toAddress;

    /**
     * 当前物流状态/位置
     */
    @Column(length = 200)
    private String currentLocation;

    /**
     * 物流状态描述
     */
    @Column(columnDefinition = "TEXT")
    private String statusDescription;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public SalesOrder getOrder() { return order; }
    public void setOrder(SalesOrder order) { this.order = order; }
    public String getTrackingNo() { return trackingNo; }
    public void setTrackingNo(String trackingNo) { this.trackingNo = trackingNo; }
    public String getLogisticsCompany() { return logisticsCompany; }
    public void setLogisticsCompany(String logisticsCompany) { this.logisticsCompany = logisticsCompany; }
    public String getFromAddress() { return fromAddress; }
    public void setFromAddress(String fromAddress) { this.fromAddress = fromAddress; }
    public String getToAddress() { return toAddress; }
    public void setToAddress(String toAddress) { this.toAddress = toAddress; }
    public String getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(String currentLocation) { this.currentLocation = currentLocation; }
    public String getStatusDescription() { return statusDescription; }
    public void setStatusDescription(String statusDescription) { this.statusDescription = statusDescription; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
