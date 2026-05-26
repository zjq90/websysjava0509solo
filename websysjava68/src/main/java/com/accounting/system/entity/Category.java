package com.accounting.system.entity;

import com.accounting.system.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分类实体类
 * 收入分类：工资、奖金、投资收益、兼职等
 * 支出分类：餐饮、交通、购物、娱乐、房贷/租金等
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("category")
@ApiModel(value = "分类")
public class Category extends BaseEntity {

    @ApiModelProperty(value = "分类名称", example = "餐饮")
    private String categoryName;

    @ApiModelProperty(value = "分类类型：INCOME-收入，EXPENSE-支出", example = "EXPENSE")
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
