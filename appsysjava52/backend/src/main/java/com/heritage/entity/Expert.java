package com.heritage.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 专家实体类
 * 
 * @author Heritage Team
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "expert")
public class Expert extends BaseEntity {

    /**
     * 关联用户ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 专家姓名
     */
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    /**
     * 职称/头衔
     */
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    /**
     * 所属机构
     */
    @Column(name = "institution", nullable = false, length = 200)
    private String institution;

    /**
     * 专家简介
     */
    @Column(name = "bio", length = 2000)
    private String bio;

    /**
     * 擅长文物类别（多个用逗号分隔）
     */
    @Column(name = "specialties", length = 500)
    private String specialties;

    /**
     * 从业年限
     */
    @Column(name = "experience_years")
    private Integer experienceYears;

    /**
     * 鉴定成功次数
     */
    @Column(name = "success_count", nullable = false)
    private Integer successCount = 0;

    /**
     * 状态：0-审核中，1-已认证，2-已拒绝
     */
    @Column(name = "status", nullable = false)
    private Integer status = 0;

    /**
     * 头像URL
     */
    @Column(name = "avatar", length = 500)
    private String avatar;
}