package com.inventory.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 品种实体类
 * 功能：管理具体的种子品种，属于某个品类
 */
@Entity
@Table(name = "variety")
public class Variety {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull(message = "所属品类不能为空")
    private Category category;

    @Column(nullable = false, unique = true, length = 50)
    @NotBlank(message = "品种编码不能为空")
    @Size(min = 1, max = 50, message = "品种编码长度必须在1-50之间")
    private String varietyCode;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "品种名称不能为空")
    @Size(min = 1, max = 100, message = "品种名称长度必须在1-100之间")
    private String varietyName;

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
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public String getVarietyCode() { return varietyCode; }
    public void setVarietyCode(String varietyCode) { this.varietyCode = varietyCode; }
    public String getVarietyName() { return varietyName; }
    public void setVarietyName(String varietyName) { this.varietyName = varietyName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
