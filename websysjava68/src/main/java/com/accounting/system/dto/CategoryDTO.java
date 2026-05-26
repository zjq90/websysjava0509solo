package com.accounting.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 分类请求DTO
 */
@Data
@ApiModel(value = "分类请求")
public class CategoryDTO {

    @ApiModelProperty(value = "分类ID（编辑时传入）")
    private Long id;

    @NotBlank(message = "分类名称不能为空")
    @Size(max = 50, message = "分类名称长度不能超过50个字符")
    @ApiModelProperty(value = "分类名称", required = true, example = "餐饮")
    private String categoryName;

    @NotBlank(message = "分类类型不能为空")
    @ApiModelProperty(value = "分类类型：INCOME-收入，EXPENSE-支出", required = true, example = "EXPENSE")
    private String categoryType;

    @ApiModelProperty(value = "父分类ID，支持多级分类")
    private Long parentId;

    @ApiModelProperty(value = "分类图标", example = "🍜")
    private String icon;

    @ApiModelProperty(value = "排序顺序", example = "1")
    private Integer sortOrder;

    @ApiModelProperty(value = "备注说明")
    private String remark;
}
