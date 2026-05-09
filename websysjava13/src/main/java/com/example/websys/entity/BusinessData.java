package com.example.websys.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 经营数据实体类
 * 存储各类经营统计数据，供移动端看板展示使用
 */
@Entity
@Table(name = "business_data")
public class BusinessData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stat_code", nullable = false, length = 50)
    private String statCode;

    @Column(name = "data_date", nullable = false)
    private LocalDate dataDate;

    @Column(name = "data_value", precision = 18, scale = 2)
    private BigDecimal dataValue;

    @Column(name = "data_text", length = 500)
    private String dataText;

    @Column(name = "trend_value", precision = 18, scale = 2)
    private BigDecimal trendValue;

    @Column(name = "trend_type", length = 10)
    private String trendType;

    @Column(name = "compare_value", precision = 18, scale = 2)
    private BigDecimal compareValue;

    @Column(name = "compare_type", length = 10)
    private String compareType;

    @Column(name = "remark", length = 500)
    private String remark;

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    public BusinessData() {
    }

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatCode() {
        return statCode;
    }

    public void setStatCode(String statCode) {
        this.statCode = statCode;
    }

    public LocalDate getDataDate() {
        return dataDate;
    }

    public void setDataDate(LocalDate dataDate) {
        this.dataDate = dataDate;
    }

    public BigDecimal getDataValue() {
        return dataValue;
    }

    public void setDataValue(BigDecimal dataValue) {
        this.dataValue = dataValue;
    }

    public String getDataText() {
        return dataText;
    }

    public void setDataText(String dataText) {
        this.dataText = dataText;
    }

    public BigDecimal getTrendValue() {
        return trendValue;
    }

    public void setTrendValue(BigDecimal trendValue) {
        this.trendValue = trendValue;
    }

    public String getTrendType() {
        return trendType;
    }

    public void setTrendType(String trendType) {
        this.trendType = trendType;
    }

    public BigDecimal getCompareValue() {
        return compareValue;
    }

    public void setCompareValue(BigDecimal compareValue) {
        this.compareValue = compareValue;
    }

    public String getCompareType() {
        return compareType;
    }

    public void setCompareType(String compareType) {
        this.compareType = compareType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
