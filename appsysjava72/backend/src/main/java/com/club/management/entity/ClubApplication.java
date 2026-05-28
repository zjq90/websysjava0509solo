package com.club.management.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 入团申请实体类
 *
 * @author club-management
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("club_application")
public class ClubApplication extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 社团ID
     */
    private Long clubId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 申请理由
     */
    private String reason;

    /**
     * 个人简介
     */
    private String resume;

    /**
     * 作品链接
     */
    private String works;

    /**
     * 审核状态 0待审核 1已通过 2已拒绝
     */
    private Integer status;

    /**
     * 审核意见
     */
    private String reviewRemark;

    /**
     * 审核人ID
     */
    private Long reviewerId;

    /**
     * 审核时间
     */
    private LocalDateTime reviewTime;
}
