package com.club.entity;

import com.club.entity.enums.ClubCategoryEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 社团实体类
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "t_club")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "社团信息")
public class Club extends BaseEntity {

    @Schema(description = "社团名称", example = "计算机协会")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Schema(description = "社团Logo", example = "https://example.com/club-logo.png")
    @Column(name = "logo", length = 500)
    private String logo;

    @Schema(description = "社团分类", example = "ACADEMIC_TECHNOLOGY")
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 50)
    private ClubCategoryEnum category;

    @Schema(description = "社团简介", example = "致力于推广计算机技术，培养学生编程能力")
    @Column(name = "description", nullable = false, length = 2000)
    private String description;

    @Schema(description = "社团宗旨", example = "技术为本，服务同学")
    @Column(name = "purpose", length = 500)
    private String purpose;

    @Schema(description = "现任负责人ID", example = "1")
    @Column(name = "leader_id")
    private Long leaderId;

    @Schema(description = "现任负责人姓名", example = "张三")
    @Column(name = "leader_name", length = 50)
    private String leaderName;

    @Schema(description = "联系电话", example = "13800138000")
    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    @Schema(description = "联系邮箱", example = "club@example.com")
    @Column(name = "contact_email", length = 100)
    private String contactEmail;

    @Schema(description = "QQ群号", example = "123456789")
    @Column(name = "qq_group", length = 20)
    private String qqGroup;

    @Schema(description = "微信号", example = "club_wechat")
    @Column(name = "wechat", length = 50)
    private String wechat;

    @Schema(description = "学校ID", example = "1")
    @Column(name = "school_id", nullable = false)
    private Long schoolId;

    @Schema(description = "成员数量", example = "150")
    @Column(name = "member_count", nullable = false)
    private Integer memberCount = 0;

    @Schema(description = "关注数量", example = "300")
    @Column(name = "follow_count", nullable = false)
    private Integer followCount = 0;

    @Schema(description = "成立日期", example = "2010-09-01")
    @Column(name = "establish_date", length = 20)
    private String establishDate;

    @Schema(description = "是否开启招新：0-关闭 1-开启", example = "1")
    @Column(name = "recruiting", nullable = false)
    private Integer recruiting = 0;

    @Schema(description = "状态：0-待审核 1-已审核 2-已禁用", example = "1")
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    @Schema(description = "浏览次数", example = "5000")
    @Column(name = "view_count", nullable = false)
    private Integer viewCount = 0;
}
