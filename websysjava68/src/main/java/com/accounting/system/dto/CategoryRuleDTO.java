package com.accounting.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 分类规则请求DTO
 */
@Data
@ApiModel(value = "分类规则请求")
public class CategoryRuleDTO {

    @ApiModelProperty(value = "规则ID（编辑时传入）")
    private Long id;

    @NotBlank(message = "规则名称不能为空")
    @Size(max = 50, message = "规则名称长度不能超过50个字符")
    @ApiModelProperty(value = "规则名称", required = true, example = "超市购物规则")
    private String ruleName;

    @NotBlank(message = "匹配字段不能为空")
    @ApiModelProperty(value = "匹配字段：DESCRIPTION-描述，MERCHANT-商家，LOCATION-地点", required = true, example = "MERCHANT")
    private String matchField;

    @NotBlank(message = "匹配类型不能为空")
    @ApiModelProperty(value = "匹配类型：CONTAINS-包含，EQUALS-等于，REGEX-正则表达式", required = true, example = "CONTAINS")
    private String matchType;

    @NotBlank(message = "匹配值不能为空")
    @ApiModelProperty(value = "匹配值/正则表达式", required = true, example = "超市")
    private String matchValue;

    @NotNull(message = "目标分类ID不能为空")
    @ApiModelProperty(value = "目标分类ID", required = true, example = "13")
    private Long targetCategoryId;

    @ApiModelProperty(value = "优先级，数值越大优先级越高", example = "10")
    private Integer priority;

    @ApiModelProperty(value = "是否启用：0-禁用，1-启用", example = "1")
    private Integer isEnabled;

    @ApiModelProperty(value = "备注说明")
    private String remark;
}
