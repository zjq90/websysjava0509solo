package com.accounting.system.entity;

import com.accounting.system.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分类规则实体类
 * 自定义匹配规则，支持正则表达式精准识别复杂商家名称
 * 如"含'超市'关键词的支出归为'购物'"
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("category_rule")
@ApiModel(value = "分类规则")
public class CategoryRule extends BaseEntity {

    @ApiModelProperty(value = "规则名称", example = "超市购物规则")
    private String ruleName;

    @ApiModelProperty(value = "匹配字段：DESCRIPTION-描述，MERCHANT-商家，LOCATION-地点", example = "MERCHANT")
    private String matchField;

    @ApiModelProperty(value = "匹配类型：CONTAINS-包含，EQUALS-等于，REGEX-正则表达式", example = "CONTAINS")
    private String matchType;

    @ApiModelProperty(value = "匹配值/正则表达式", example = "超市")
    private String matchValue;

    @ApiModelProperty(value = "目标分类ID", example = "13")
    private Long targetCategoryId;

    @ApiModelProperty(value = "优先级，数值越大优先级越高", example = "10")
    private Integer priority;

    @ApiModelProperty(value = "是否启用：0-禁用，1-启用", example = "1")
    private Integer isEnabled;

    @ApiModelProperty(value = "备注说明")
    private String remark;
}
