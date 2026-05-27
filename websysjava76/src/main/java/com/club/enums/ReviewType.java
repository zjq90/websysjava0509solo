package com.club.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 内容审查类型枚举
 *
 * @author Club Management System
 * @version 1.0.0
 */
@Schema(description = "内容审查类型")
public enum ReviewType {

    @Schema(description = "活动描述")
    DESCRIPTION("活动描述"),

    @Schema(description = "海报内容")
    POSTER("海报内容"),

    @Schema(description = "活动主题")
    THEME("活动主题"),

    @Schema(description = "活动名称")
    NAME("活动名称");

    private final String description;

    ReviewType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
