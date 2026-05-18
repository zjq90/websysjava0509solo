package com.psyconsult.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "appointment")
@ApiModel(description = "预约实体")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "预约ID")
    private Long id;

    @Column(name = "user_id", nullable = false)
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @Column(name = "counselor_id", nullable = false)
    @ApiModelProperty(value = "咨询师ID")
    private Long counselorId;

    @Column(name = "schedule_id")
    @ApiModelProperty(value = "关联日程ID")
    private Long scheduleId;

    @Column(nullable = false)
    @ApiModelProperty(value = "预约日期")
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    @ApiModelProperty(value = "开始时间")
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    @ApiModelProperty(value = "结束时间")
    private LocalTime endTime;

    @Column(length = 500)
    @ApiModelProperty(value = "用户主诉")
    private String chiefComplaint;

    @Column(name = "contact_phone", length = 20)
    @ApiModelProperty(value = "联系电话")
    private String contactPhone;

    @Column(length = 20)
    @ApiModelProperty(value = "预约状态：pending-待确认，confirmed-已确认，completed-已完成，cancelled-已取消")
    private String status = "pending";

    @Column(length = 500)
    @ApiModelProperty(value = "取消原因")
    private String cancelReason;

    @Column(name = "create_time")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
