package com.club.management.fund.entity;

import com.club.management.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 经费记录实体类
 * 记录每一笔收支，包含收入和支出
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "fund_record")
@EqualsAndHashCode(callSuper = true)
public class FundRecord extends BaseEntity {

    /**
     * 记录编号
     */
    private String recordNo;

    /**
     * 社团ID
     */
    private Long clubId;

    /**
     * 社团名称
     */
    private String clubName;

    /**
     * 收支类型 0-支出 1-收入
     */
    private Integer type;

    /**
     * 金额
     */
    private Double amount;

    /**
     * 余额
     */
    private Double balance;

    /**
     * 分类：
     * 收入类：0-会费 1-赞助 2-拨款 3-其他收入
     * 支出类：10-活动物料 11-场地费 12-宣传费用 13-差旅费用 14-其他支出
     */
    private Integer category;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 发生日期
     */
    private String occurDate;

    /**
     * 摘要/说明
     */
    private String summary;

    /**
     * 关联活动ID
     */
    private Long activityId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 经办人ID
     */
    private Long handlerId;

    /**
     * 经办人姓名
     */
    private String handlerName;

    /**
     * 发票/收据凭证URL，多个用逗号分隔
     */
    private String vouchers;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态 0-待审核 1-已通过 2-已拒绝
     */
    private Integer status;

    /**
     * 审核人ID
     */
    private Long auditorId;

    /**
     * 审核人姓名
     */
    private String auditorName;

    /**
     * 审核时间
     */
    private String auditTime;

    /**
     * 审核意见
     */
    private String auditOpinion;
}
