package com.inventory.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 门店实体类
 * 功能：管理多个门店信息，支持多门店库存同步
 */
@Entity
@Table(name = "store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @NotBlank(message = "门店编码不能为空")
    @Size(min = 1, max = 50, message = "门店编码长度必须在1-50之间")
    private String storeCode;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "门店名称不能为空")
    @Size(min = 1, max = 100, message = "门店名称长度必须在1-100之间")
    private String storeName;

    @Column(length = 200)
    @Size(max = 200, message = "地址长度不能超过200")
    private String address;

    @Column(length = 50)
    @Size(max = 50, message = "负责人长度不能超过50")
    private String manager;

    @Column(length = 20)
    @Pattern(regexp = "^$|^1[3-9]\\d{9}$|^\\d{3,4}-\\d{7,8}$", message = "电话号码格式不正确")
    private String phone;

    @Column(length = 500)
    @Size(max = 500, message = "描述长度不能超过500")
    private String description;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getStoreCode() { return storeCode; }
    public void setStoreCode(String storeCode) { this.storeCode = storeCode; }
    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getManager() { return manager; }
    public void setManager(String manager) { this.manager = manager; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
