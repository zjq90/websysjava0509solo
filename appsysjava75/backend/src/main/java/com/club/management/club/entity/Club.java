package com.club.management.club.entity;

import com.club.management.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 社团实体类
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "club")
@EqualsAndHashCode(callSuper = true)
public class Club extends BaseEntity {

    /**
     * 社团名称
     */
    private String name;

    /**
     * 社团logo
     */
    private String logo;

    /**
     * 社团类型 0-学术科技 1-文艺体育 2-公益实践 3-创新创业 4-其他
     */
    private Integer type;

    /**
     * 社团简介
     */
    private String description;

    /**
     * 社团宗旨
     */
    private String purpose;

    /**
     * 负责人ID
     */
    private Long leaderId;

    /**
     * 负责人姓名
     */
    private String leaderName;

    /**
     * 指导老师
     */
    private String advisor;

    /**
     * 成员数量
     */
    private Integer memberCount;

    /**
     * 成立时间
     */
    private String establishDate;

    /**
     * 状态 0-待审核 1-正常 2-冻结 3-解散
     */
    private Integer status;

    /**
     * 公告
     */
    private String announcement;
}
