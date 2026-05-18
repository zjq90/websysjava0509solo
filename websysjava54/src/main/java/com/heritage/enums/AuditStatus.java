package com.heritage.enums;

public enum AuditStatus {
    PENDING("待审核"),
    APPROVED("审核通过"),
    REJECTED("审核拒绝"),
    REVIEWING("审核中");

    private String description;

    AuditStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
