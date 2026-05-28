package com.club.management.fund.entity;

import com.club.management.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 报销申请实体类
 * 成员提交报销申请，管理者在线审批
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "reimbursement")
@EqualsAndHashCode(callSuper = true)
public class Reimbursement extends BaseEntity {

    /**
     * 报销单号
     */
    private String reimburseNo;

    /**
     * 社团ID
     */
    private Long clubId;

    /**
     * 社团名称
     */
    private String clubName;

    /**
     * 申请人ID
     */
    private Long applicantId;

    /**
     * 申请人姓名
     */
    private String applicantName;

    /**
     * 申请金额
     */
    private Double amount;

    /**
     * 报销分类 0-活动物料 1-场地费 2-宣传费用 3-差旅费用 4-其他
     */
    private Integer category;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 报销事由
     */
    private String reason;

    /**
     * 发生日期
     */
    private String occurDate;

    /**
     * 关联活动ID
     */
    private Long activityId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 发票/收据凭证URL，多个用逗号分隔
     */
    private String vouchers;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态 0-待审核 1-已通过 2-已拒绝 3-已付款
     */
    private Integer status;

    /**
     * 当前审批节点
     */
    private Integer currentNode;

    /**
     * 审批人ID
     */
    private Long auditorId;

    /**
     * 审批人姓名
     */
    private String auditorName;

    /**
     * 审批时间
     */
    private String auditTime;

    /**
     * 审批意见
     */
    private String auditOpinion;

    /**
     * 关联经费记录ID（审批通过后生成）
     */
    private Long fundRecordId;
}
