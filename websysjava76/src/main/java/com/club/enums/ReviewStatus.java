package com.club.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 内容审查状态枚举
 *
 * @author Club Management System
 * @version 1.0.0
 */
@Schema(description = "内容审查状态")
public enum ReviewStatus {

    @Schema(description = "待审核")
    PENDING("待审核"),

    @Schema(description = "已通过")
    APPROVED("已通过"),

    @Schema(description = "已屏蔽")
    BLOCKED("已屏蔽"),

    @Schema(description = "已修改")
    MODIFIED("已修改");

    private final String description;

    ReviewStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
