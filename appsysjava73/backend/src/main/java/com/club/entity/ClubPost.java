package com.club.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 社团讨论区帖子实体类
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "t_club_post")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "社团讨论区帖子")
public class ClubPost extends BaseEntity {

    @Schema(description = "社团ID", example = "1")
    @Column(name = "club_id", nullable = false)
    private Long clubId;

    @Schema(description = "发布人ID", example = "1")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Schema(description = "发布人用户名", example = "student001")
    @Column(name = "username", length = 50)
    private String username;

    @Schema(description = "发布人真实姓名", example = "张三")
    @Column(name = "real_name", length = 50)
    private String realName;

    @Schema(description = "发布人头像", example = "https://example.com/avatar.png")
    @Column(name = "avatar", length = 500)
    private String avatar;

    @Schema(description = "帖子标题", example = "关于本周活动的通知")
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Schema(description = "帖子内容", example = "本周六下午2点在会议室召开部门例会，请大家准时参加")
    @Column(name = "content", nullable = false, length = 5000)
    private String content;

    @Schema(description = "帖子类型：通知/讨论/求助/分享", example = "通知")
    @Column(name = "type", length = 50)
    private String type;

    @Schema(description = "是否置顶：0-否 1-是", example = "0")
    @Column(name = "top", nullable = false)
    private Integer top = 0;

    @Schema(description = "是否精华：0-否 1-是", example = "0")
    @Column(name = "essence", nullable = false)
    private Integer essence = 0;

    @Schema(description = "浏览次数", example = "150")
    @Column(name = "view_count", nullable = false)
    private Integer viewCount = 0;

    @Schema(description = "点赞次数", example = "20")
    @Column(name = "like_count", nullable = false)
    private Integer likeCount = 0;

    @Schema(description = "评论次数", example = "10")
    @Column(name = "comment_count", nullable = false)
    private Integer commentCount = 0;

    @Schema(description = "状态：0-正常 1-已删除", example = "0")
    @Column(name = "status", nullable = false)
    private Integer status = 0;
}
