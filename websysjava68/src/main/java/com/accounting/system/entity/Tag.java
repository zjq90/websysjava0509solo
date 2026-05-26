package com.accounting.system.entity;

import com.accounting.system.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 标签实体类
 * 支持自定义标签，如"旅行""生日礼物""医疗"等
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tag")
@ApiModel(value = "标签")
public class Tag extends BaseEntity {

    @ApiModelProperty(value = "标签名称", example = "旅行")
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
