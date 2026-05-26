package com.accounting.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "账单类型")
public enum BillType {

    @Schema(description = "支出")
    EXPENSE("支出"),

    @Schema(description = "收入")
    INCOME("收入");

    private final String description;

    BillType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
