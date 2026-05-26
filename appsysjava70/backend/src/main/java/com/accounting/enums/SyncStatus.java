package com.accounting.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "同步状态")
public enum SyncStatus {

    @Schema(description = "已同步")
    SYNCED("已同步"),

    @Schema(description = "待同步")
    PENDING("待同步"),

    @Schema(description = "同步失败")
    FAILED("同步失败");

    private final String description;

    SyncStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
