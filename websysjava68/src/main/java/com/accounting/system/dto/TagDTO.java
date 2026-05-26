package com.accounting.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 标签请求DTO
 */
@Data
@ApiModel(value = "标签请求")
public class TagDTO {

    @ApiModelProperty(value = "标签ID（编辑时传入）")
    private Long id;

    @NotBlank(message = "标签名称不能为空")
    @Size(max = 50, message = "标签名称长度不能超过50个字符")
    @ApiModelProperty(value = "标签名称", required = true, example = "旅行")
    private String tagName;

    @ApiModelProperty(value = "标签颜色", example = "#FF6B6B")
    private String tagColor;

    @ApiModelProperty(value = "标签类型：CUSTOM-自定义，SYSTEM-系统", example = "CUSTOM")
    private String tagType;

    @ApiModelProperty(value = "标签图标", example = "✈️")
    private String icon;

    @ApiModelProperty(value = "排序顺序", example = "1")
    private Integer sortOrder;

    @ApiModelProperty(value = "备注说明")
    private String remark;
}
