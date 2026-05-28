package com.club.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 帖子评论实体类
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "t_club_post_comment")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "帖子评论")
public class ClubPostComment extends BaseEntity {

    @Schema(description = "帖子ID", example = "1")
    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Schema(description = "社团ID", example = "1")
    @Column(name = "club_id", nullable = false)
    private Long clubId;

    @Schema(description = "评论人ID", example = "1")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Schema(description = "评论人用户名", example = "student001")
    @Column(name = "username", length = 50)
    private String username;

    @Schema(description = "评论人真实姓名", example = "张三")
    @Column(name = "real_name", length = 50)
    private String realName;

    @Schema(description = "评论人头像", example = "https://example.com/avatar.png")
    @Column(name = "avatar", length = 500)
    private String avatar;

    @Schema(description = "评论内容", example = "收到，准时参加！")
    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @Schema(description = "父评论ID，0表示一级评论", example = "0")
    @Column(name = "parent_id", nullable = false)
    private Long parentId = 0L;

    @Schema(description = "回复目标用户ID", example = "2")
    @Column(name = "reply_to_user_id")
    private Long replyToUserId;

    @Schema(description = "回复目标用户名", example = "李四")
    @Column(name = "reply_to_username", length = 50)
    private String replyToUsername;

    @Schema(description = "点赞次数", example = "5")
    @Column(name = "like_count", nullable = false)
    private Integer likeCount = 0;

    @Schema(description = "状态：0-正常 1-已删除", example = "0")
    @Column(name = "status", nullable = false)
    private Integer status = 0;
}
