package com.sales.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 客户实体类
 * 存储客户档案信息，支持分级与标签化管理
 */
@Entity
@Table(name = "customers")
public class Customer {

    /**
     * 客户ID，主键，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 客户名称
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * 客户编码，唯一标识
     */
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    /**
     * 客户类型：农户、合作社、经销商
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CustomerType type;

    /**
     * 客户等级：普通、优质、核心
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CustomerLevel level = CustomerLevel.NORMAL;

    /**
     * 联系人
     */
    @Column(length = 50)
    private String contactPerson;

    /**
     * 联系电话
     */
    @Column(length = 20)
    private String phone;

    /**
     * 电子邮箱
     */
    @Column(length = 100)
    private String email;

    /**
     * 省份
     */
    @Column(length = 50)
    private String province;

    /**
     * 城市
     */
    @Column(length = 50)
    private String city;

    /**
     * 详细地址
     */
    @Column(length = 200)
    private String address;

    /**
     * 客户标签（多个标签用逗号分隔）
     */
    @Column(length = 200)
    private String tags;

    /**
     * 客户备注
     */
    @Column(columnDefinition = "TEXT")
    private String remark;

    /**
     * 客户状态（启用/禁用）
     */
    @Column(nullable = false)
    private Boolean active = true;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 客户订单列表
     */
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<SalesOrder> orders = new HashSet<>();

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
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public CustomerType getType() { return type; }
    public void setType(CustomerType type) { this.type = type; }
    public CustomerLevel getLevel() { return level; }
    public void setLevel(CustomerLevel level) { this.level = level; }
    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public Set<SalesOrder> getOrders() { return orders; }
    public void setOrders(Set<SalesOrder> orders) { this.orders = orders; }
}
