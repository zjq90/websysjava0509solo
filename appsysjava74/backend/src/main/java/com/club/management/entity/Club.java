package com.club.management.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 社团实体类
 * 
 * @author club-management
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "club")
public class Club extends BaseEntity {

    /**
     * 社团名称
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * 社团简称
     */
    @Column(name = "short_name", length = 50)
    private String shortName;

    /**
     * 社团类型（学术、文艺、体育、科技等）
     */
    @Column(name = "type", length = 50)
    private String type;

    /**
     * 社团简介
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * 社团Logo URL
     */
    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    /**
     * 社团封面图片URL
     */
    @Column(name = "cover_url", length = 500)
    private String coverUrl;

    /**
     * 指导老师
     */
    @Column(name = "advisor", length = 50)
    private String advisor;

    /**
     * 联系电话
     */
    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    /**
     * 联系邮箱
     */
    @Column(name = "contact_email", length = 100)
    private String contactEmail;

    /**
     * 社团人数
     */
    @Column(name = "member_count")
    private Integer memberCount = 0;

    /**
     * 社团状态（0：审核中，1：已通过，2：已拒绝，3：已注销）
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    /**
     * 社长用户ID
     */
    @Column(name = "president_id")
    private Long presidentId;
}
