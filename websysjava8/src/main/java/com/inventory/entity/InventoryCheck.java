package com.inventory.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 盘点实体类
 * 功能：管理库存盘点记录
 */
@Entity
@Table(name = "inventory_check")
public class InventoryCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @NotBlank(message = "盘点单号不能为空")
    @Size(min = 1, max = 50, message = "盘点单号长度必须在1-50之间")
    private String checkCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inventory_id", nullable = false)
    @NotNull(message = "库存不能为空")
    private Inventory inventory;

    @Column(nullable = false)
    @NotNull(message = "系统数量不能为空")
    @Min(value = 0, message = "系统数量不能为负数")
    private Integer systemQuantity;

    @Column(nullable = false)
    @NotNull(message = "实际数量不能为空")
    @Min(value = 0, message = "实际数量不能为负数")
    private Integer actualQuantity;

    @Column(nullable = false)
    @NotNull(message = "差异数量不能为空")
    private Integer diffQuantity;

    @Column(length = 20)
    @Size(max = 20, message = "差异类型长度不能超过20")
    private String diffType;

    @Column(length = 500)
    @Size(max = 500, message = "备注长度不能超过500")
    private String remark;

    @Column(length = 20)
    @Size(max = 20, message = "状态长度不能超过20")
    private String status;

    private LocalDateTime checkTime;

    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        if (checkTime == null) {
            checkTime = LocalDateTime.now();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCheckCode() { return checkCode; }
    public void setCheckCode(String checkCode) { this.checkCode = checkCode; }
    public Inventory getInventory() { return inventory; }
    public void setInventory(Inventory inventory) { this.inventory = inventory; }
    public Integer getSystemQuantity() { return systemQuantity; }
    public void setSystemQuantity(Integer systemQuantity) { this.systemQuantity = systemQuantity; }
    public Integer getActualQuantity() { return actualQuantity; }
    public void setActualQuantity(Integer actualQuantity) { this.actualQuantity = actualQuantity; }
    public Integer getDiffQuantity() { return diffQuantity; }
    public void setDiffQuantity(Integer diffQuantity) { this.diffQuantity = diffQuantity; }
    public String getDiffType() { return diffType; }
    public void setDiffType(String diffType) { this.diffType = diffType; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCheckTime() { return checkTime; }
    public void setCheckTime(LocalDateTime checkTime) { this.checkTime = checkTime; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
