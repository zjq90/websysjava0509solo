package com.club.management.square.entity;

import com.club.management.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 点赞实体类
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "like_record")
@EqualsAndHashCode(callSuper = true)
public class LikeRecord extends BaseEntity {

    /**
     * 业务类型 0-活动圈 1-话题帖子 2-学长分享 3-评论
     */
    private Integer businessType;

    /**
     * 业务ID
     */
    private Long businessId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;
}
