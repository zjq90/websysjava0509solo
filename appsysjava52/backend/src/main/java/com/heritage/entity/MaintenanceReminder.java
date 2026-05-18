package com.heritage.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * 保养提醒实体类
 * 
 * @author Heritage Team
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "maintenance_reminder")
public class MaintenanceReminder extends BaseEntity {

    /**
     * 关联收藏ID
     */
    @Column(name = "collection_item_id", nullable = false)
    private Long collectionItemId;

    /**
     * 保养类型：1-日常清洁，2-防潮处理，3-防蛀处理，4-修复保养，5-其他
     */
    @Column(name = "maintenance_type", nullable = false)
    private Integer maintenanceType;

    /**
     * 保养标题
     */
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    /**
     * 保养内容
     */
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    /**
     * 权威文献引用
     */
    @Column(name = "reference", length = 500)
    private String reference;

    /**
     * 计划保养日期
     */
    @Column(name = "scheduled_date", nullable = false)
    private LocalDate scheduledDate;

    /**
     * 提醒日期
     */
    @Column(name = "remind_date")
    private LocalDate remindDate;

    /**
     * 实际完成日期
     */
    @Column(name = "completed_date")
    private LocalDate completedDate;

    /**
     * 状态：0-待提醒，1-已提醒，2-已完成，3-已取消
     */
    @Column(name = "status", nullable = false)
    private Integer status = 0;

    /**
     * 备注
     */
    @Column(name = "remark", length = 1000)
    private String remark;
}