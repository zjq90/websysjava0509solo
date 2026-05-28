package com.club.management.fund.entity;

import com.club.management.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 经费统计报表实体类
 * 自动生成经费统计报表，按月份分类展示收支
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "fund_statistics")
@EqualsAndHashCode(callSuper = true)
public class FundStatistics extends BaseEntity {

    /**
     * 社团ID
     */
    private Long clubId;

    /**
     * 社团名称
     */
    private String clubName;

    /**
     * 统计类型 0-日报 1-周报 2-月报 3-季报 4-年报
     */
    private Integer statisticsType;

    /**
     * 统计周期，如2024-01
     */
    private String period;

    /**
     * 期初余额
     */
    private Double openingBalance;

    /**
     * 期末余额
     */
    private Double closingBalance;

    /**
     * 总收入
     */
    private Double totalIncome;

    /**
     * 总支出
     */
    private Double totalExpense;

    /**
     * 净收入
     */
    private Double netIncome;

    /**
     * 收入分类统计JSON
     * {"会费": 1000, "赞助": 5000, "拨款": 0, "其他收入": 200}
     */
    private String incomeCategoryStats;

    /**
     * 支出分类统计JSON
     * {"活动物料": 800, "场地费": 500, "宣传费用": 300, "其他": 200}
     */
    private String expenseCategoryStats;

    /**
     * 收入笔数
     */
    private Integer incomeCount;

    /**
     * 支出笔数
     */
    private Integer expenseCount;

    /**
     * 最大单笔收入
     */
    private Double maxIncome;

    /**
     * 最大单笔支出
     */
    private Double maxExpense;
}
