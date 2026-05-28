package com.club.management.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 动态点赞实体类
 *
 * @author club-management
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("feed_like")
public class FeedLike extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 动态ID
     */
    private Long feedId;

    /**
     * 用户ID
     */
    private Long userId;
}
