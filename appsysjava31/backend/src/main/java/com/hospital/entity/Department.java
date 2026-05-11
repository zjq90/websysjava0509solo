package com.hospital.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 科室实体类
 * 存储医院科室信息
 * 
 * @author hospital
 * @version 1.0.0
 */
@Entity
@Table(name = "sys_department")
public class Department {

    /**
     * 科室ID（主键）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 科室编号
     */
    @Column(unique = true, nullable = false, length = 20)
    private String deptCode;

    /**
     * 科室名称
     */
    @Column(nullable = false, length = 100)
    private String deptName;

    /**
     * 科室类型：INTERNAL-内科，SURGERY-外科，EMERGENCY-急诊，OTHER-其他
     */
    @Column(length = 20)
    private String deptType;

    /**
     * 父科室ID（支持多级科室）
     */
    private Long parentId;

    /**
     * 科室位置描述
     */
    @Column(length = 200)
    private String location;

    /**
     * 科室简介
     */
    @Column(length = 1000)
    private String description;

    /**
     * 科室图标URL
     */
    @Column(length = 500)
    private String icon;

    /**
     * 排序序号
     */
    private Integer sortOrder = 0;

    /**
     * 状态：1-启用，0-禁用
     */
    @Column(nullable = false)
    private Integer status = 1;

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
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }

    public Department() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getName() {
        return deptName;
    }

    public void setName(String name) {
        this.deptName = name;
    }

    public String getDeptType() {
        return deptType;
    }

    public void setDeptType(String deptType) {
        this.deptType = deptType;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
