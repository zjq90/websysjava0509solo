package com.club.management.square.entity;

import com.club.management.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 活动圈动态实体类
 * 用户可发布参与活动的动态，上传照片
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "activity_post")
@EqualsAndHashCode(callSuper = true)
public class ActivityPost extends BaseEntity {

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
     * 关联活动ID
     */
    private Long activityId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 社团ID
     */
    private Long clubId;

    /**
     * 社团名称
     */
    private String clubName;

    /**
     * 动态内容
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
     * 状态 0-正常 1-已删除 2-审核中
     */
    private Integer status;

    /**
     * 位置信息
     */
    private String location;
}
