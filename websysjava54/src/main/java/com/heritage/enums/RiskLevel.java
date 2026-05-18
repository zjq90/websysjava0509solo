package com.heritage.enums;

public enum RiskLevel {
    HIGH("高风险", 3),
    MEDIUM("中风险", 2),
    LOW("低风险", 1);

    private String description;
    private int level;

    RiskLevel(String description, int level) {
        this.description = description;
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public int getLevel() {
        return level;
    }
}
