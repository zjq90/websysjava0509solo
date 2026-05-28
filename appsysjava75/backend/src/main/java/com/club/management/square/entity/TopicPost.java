package com.club.management.square.entity;

import com.club.management.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 话题帖子实体类
 * 用户可在话题下发帖讨论
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "topic_post")
@EqualsAndHashCode(callSuper = true)
public class TopicPost extends BaseEntity {

    /**
     * 话题ID
     */
    private Long topicId;

    /**
     * 话题名称
     */
    private String topicName;

    /**
     * 发布用户ID
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
     * 帖子标题
     */
    private String title;

    /**
     * 帖子内容
     */
    private String content;

    /**
     * 图片URL，多个用逗号分隔
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

    /**
     * 浏览数
     */
    private Integer viewCount;

    /**
     * 是否精华 0-否 1-是
     */
    private Integer isEssence;

    /**
     * 是否置顶 0-否 1-是
     */
    private Integer isTop;

    /**
     * 状态 0-正常 1-已删除 2-审核中
     */
    private Integer status;
}
