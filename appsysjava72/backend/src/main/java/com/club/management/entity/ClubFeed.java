package com.club.management.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 社团动态实体类
 *
 * @author club-management
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("club_feed")
public class ClubFeed extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 社团ID
     */
    private Long clubId;

    /**
     * 发布人ID
     */
    private Long userId;

    /**
     * 动态内容
     */
    private String content;

    /**
     * 图片列表，逗号分隔
     */
    private String images;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 评论数
     */
    private Integer commentCount;
}
