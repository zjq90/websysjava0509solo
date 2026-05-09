package com.example.websys.dto;

import java.math.BigDecimal;

/**
 * 移动端看板展示项数据传输对象
 * 用于返回给前端展示的统计项及数据组合
 */
public class DashboardItemDTO {

    private Long statItemId;
    private String itemCode;
    private String itemName;
    private String itemType;
    private String unit;
    private String icon;
    private String color;
    private BigDecimal dataValue;
    private String dataText;
    private BigDecimal trendValue;
    private String trendType;
    private BigDecimal compareValue;
    private String compareType;
    private Integer sortOrder;

    public DashboardItemDTO() {
    }

    public Long getStatItemId() {
        return statItemId;
    }

    public void setStatItemId(Long statItemId) {
        this.statItemId = statItemId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}
