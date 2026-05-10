package com.appsys.entity;

import java.math.BigDecimal;

/**
 * 客户等级枚举
 * 根据客户等级自动匹配价格策略（折扣率）
 * 
 * @author appsys-team
 * @version 1.0.0
 */
public enum CustomerLevel {
    /**
     * 临时客户：展会或下乡推广时快速注册的客户，无折扣
     */
    TEMPORARY("临时客户", new BigDecimal("1.00"), 0),
    
    /**
     * 普通客户：基础会员，95折
     */
    NORMAL("普通客户", new BigDecimal("0.95"), 5000),
    
    /**
     * VIP客户：高级会员，9折
     */
    VIP("VIP客户", new BigDecimal("0.90"), 20000),
    
    /**
     * SVIP客户：超级会员，85折
     */
    SVIP("SVIP客户", new BigDecimal("0.85"), 50000),
    
    /**
     * 钻石客户：顶级会员，8折
     */
    DIAMOND("钻石客户", new BigDecimal("0.80"), 100000);

    private final String description;
    private final BigDecimal discountRate;
    private final int upgradeThreshold;

    CustomerLevel(String description, BigDecimal discountRate, int upgradeThreshold) {
        this.description = description;
        this.discountRate = discountRate;
        this.upgradeThreshold = upgradeThreshold;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public int getUpgradeThreshold() {
        return upgradeThreshold;
    }

    /**
     * 根据累计消费金额计算客户等级
     * @param totalAmount 累计消费金额
     * @return 对应的客户等级
     */
    public static CustomerLevel calculateLevel(BigDecimal totalAmount) {
        if (totalAmount == null) {
            return TEMPORARY;
        }
        int amount = totalAmount.intValue();
        if (amount >= DIAMOND.upgradeThreshold) {
            return DIAMOND;
        } else if (amount >= SVIP.upgradeThreshold) {
            return SVIP;
        } else if (amount >= VIP.upgradeThreshold) {
            return VIP;
        } else if (amount >= NORMAL.upgradeThreshold) {
            return NORMAL;
        }
        return TEMPORARY;
    }
}
