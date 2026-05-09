package com.example.websys.dto.stat;

import com.example.websys.validator.ValidColor;
import com.example.websys.validator.ValidItemCode;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 更新统计项数据传输对象
 */
public class StatItemUpdateDTO {

    @NotNull(message = "统计项ID不能为空")
    private Long id;

    @ValidItemCode(message = "编码格式不正确，必须以小写字母开头，只能包含小写字母、数字和下划线，长度3-50个字符")
    private String itemCode;

    @Size(min = 2, max = 100, message = "统计项名称长度必须在2-100个字符之间")
    private String itemName;

    private String itemType;

    @Size(max = 200, message = "数据源描述长度不能超过200个字符")
    private String dataSource;

    @Size(max = 20, message = "单位长度不能超过20个字符")
    private String unit;

    @Size(max = 50, message = "图标类名长度不能超过50个字符")
    private String icon;

    @ValidColor(required = false, message = "颜色格式不正确，请输入有效的HEX颜色值（如：#FF5733 或 #fff）")
    private String color;

    private Boolean isDefault;

    @Min(value = 0, message = "排序值不能为负数")
    private Integer sortOrder;

    private Integer status;

    @Size(max = 500, message = "描述长度不能超过500个字符")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
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

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
