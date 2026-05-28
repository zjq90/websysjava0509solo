package com.club.management.resource.entity;

import com.club.management.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 校企对接申请实体类
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "cooperation_apply")
@EqualsAndHashCode(callSuper = true)
public class CooperationApply extends BaseEntity {

    /**
     * 校企对接ID
     */
    private Long cooperationId;

    /**
     * 申请人ID
     */
    private Long applicantId;

    /**
     * 申请人姓名
     */
    private String applicantName;

    /**
     * 申请人头像
     */
    private String applicantAvatar;

    /**
     * 申请类型 0-个人申请 1-社团申请
     */
    private Integer applyType;

    /**
     * 社团ID（社团申请时使用）
     */
    private Long clubId;

    /**
     * 社团名称
     */
    private String clubName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 简历/申请材料URL
     */
    private String resumeUrl;

    /**
     * 申请理由
     */
    private String applyReason;

    /**
     * 状态 0-待审核 1-已通过 2-已拒绝 3-已结束
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
