package com.club.management.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 活动报名实体类
 *
 * @author club-management
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("activity_sign_up")
public class ActivitySignUp extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 报名时间
     */
    private LocalDateTime signUpTime;

    /**
     * 报名状态 0已取消 1已报名
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;
}
