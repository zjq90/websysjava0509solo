package com.club.management.resource.entity;

import com.club.management.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 校企对接实体类
 * 企业发布实习、赞助信息，社团可对接赞助资源
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "enterprise_cooperation")
@EqualsAndHashCode(callSuper = true)
public class EnterpriseCooperation extends BaseEntity {

    /**
     * 类型 0-实习信息 1-赞助信息 2-合作项目 3-招聘信息
     */
    private Integer type;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面图片
     */
    private String cover;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 企业logo
     */
    private String enterpriseLogo;

    /**
     * 企业简介
     */
    private String enterpriseIntro;

    /**
     * 行业领域
     */
    private String industry;

    /**
     * 企业规模
     */
    private String scale;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 联系邮箱
     */
    private String contactEmail;

    /**
     * 薪资待遇（实习/招聘）
     */
    private String salary;

    /**
     * 工作地点
     */
    private String location;

    /**
     * 赞助金额（赞助信息）
     */
    private Double sponsorAmount;

    /**
     * 赞助形式 0-资金 1-物资 2-技术支持 3-其他
     */
    private Integer sponsorForm;

    /**
     * 赞助要求
     */
    private String sponsorRequirement;

    /**
     * 岗位要求（实习/招聘）
     */
    private String jobRequirement;

    /**
     * 岗位职责
     */
    private String jobResponsibility;

    /**
     * 详细描述
     */
    private String description;

    /**
     * 发布人ID（企业代表或管理员）
     */
    private Long publisherId;

    /**
     * 发布人姓名
     */
    private String publisherName;

    /**
     * 截止日期
     */
    private String deadline;

    /**
     * 浏览数
     */
    private Integer viewCount;

    /**
     * 申请数
     */
    private Integer applyCount;

    /**
     * 收藏数
     */
    private Integer favoriteCount;

    /**
     * 是否推荐 0-否 1-是
     */
    private Integer isRecommend;

    /**
     * 是否置顶 0-否 1-是
     */
    private Integer isTop;

    /**
     * 状态 0-招聘中 1-已结束 2-已过期
     */
    private Integer status;
}
