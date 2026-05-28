package com.club.management.square.entity;

import com.club.management.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 评论实体类
 * 用于活动圈、话题、学长分享的评论
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "comment")
@EqualsAndHashCode(callSuper = true)
public class Comment extends BaseEntity {

    /**
     * 业务类型 0-活动圈 1-话题帖子 2-学长分享
     */
    private Integer businessType;

    /**
     * 业务ID
     */
    private Long businessId;

    /**
     * 评论用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 图片URL
     */
    private String image;

    /**
     * 父评论ID
     */
    private Long parentId;

    /**
     * 回复用户ID
     */
    private Long replyUserId;

    /**
     * 回复用户名
     */
    private String replyUserName;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 状态 0-正常 1-已删除
     */
    private Integer status;
}
