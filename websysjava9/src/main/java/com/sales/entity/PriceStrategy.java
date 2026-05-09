package com.sales.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 价格策略实体类
 * 管理不同客户、不同区域的销售价格策略
 */
@Entity
@Table(name = "price_strategies")
public class PriceStrategy {

    /**
     * 策略ID，主键，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 策略名称
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * 关联产品（空则为全局策略）
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    /**
     * 关联客户（空则为通用策略）
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    /**
     * 客户类型（当不指定具体客户时，按类型定价）
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CustomerType customerType;

    /**
     * 客户等级（当不指定具体客户时，按等级定价）
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CustomerLevel customerLevel;

    /**
     * 适用区域（省份，空则全国通用）
     */
    @Column(length = 50)
    private String region;

    /**
     * 优惠类型：折扣/固定价格
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PriceType priceType = PriceType.DISCOUNT;

    /**
     * 折扣比例（如0.9表示9折）
     */
    @Column(precision = 5, scale = 2)
    private BigDecimal discountRate;

    /**
     * 固定价格
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal fixedPrice;

    /**
     * 起始数量（达到该数量才适用）
     */
    private Integer minQuantity;

    /**
     * 策略状态（启用/禁用）
     */
    @Column(nullable = false)
    private Boolean active = true;

    /**
     * 优先级（数值越大优先级越高）
     */
    @Column(nullable = false)
    private Integer priority = 0;

    /**
     * 策略说明
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public CustomerType getCustomerType() { return customerType; }
    public void setCustomerType(CustomerType customerType) { this.customerType = customerType; }
    public CustomerLevel getCustomerLevel() { return customerLevel; }
    public void setCustomerLevel(CustomerLevel customerLevel) { this.customerLevel = customerLevel; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public PriceType getPriceType() { return priceType; }
    public void setPriceType(PriceType priceType) { this.priceType = priceType; }
    public BigDecimal getDiscountRate() { return discountRate; }
    public void setDiscountRate(BigDecimal discountRate) { this.discountRate = discountRate; }
    public BigDecimal getFixedPrice() { return fixedPrice; }
    public void setFixedPrice(BigDecimal fixedPrice) { this.fixedPrice = fixedPrice; }
    public Integer getMinQuantity() { return minQuantity; }
    public void setMinQuantity(Integer minQuantity) { this.minQuantity = minQuantity; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
