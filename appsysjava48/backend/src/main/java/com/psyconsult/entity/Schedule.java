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
@Table(name = "schedule")
@ApiModel(description = "日程排班实体")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "日程ID")
    private Long id;

    @Column(name = "counselor_id", nullable = false)
    @ApiModelProperty(value = "咨询师ID")
    private Long counselorId;

    @Column(nullable = false)
    @ApiModelProperty(value = "日期")
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    @ApiModelProperty(value = "开始时间")
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    @ApiModelProperty(value = "结束时间")
    private LocalTime endTime;

    @Column(name = "max_appointments")
    @ApiModelProperty(value = "最大预约数")
    private Integer maxAppointments = 1;

    @Column(name = "current_appointments")
    @ApiModelProperty(value = "当前预约数")
    private Integer currentAppointments = 0;

    @Column(name = "is_available")
    @ApiModelProperty(value = "是否可预约")
    private Boolean isAvailable = true;

    @Column(length = 200)
    @ApiModelProperty(value = "备注")
    private String remark;

    @Column(nullable = false)
    @ApiModelProperty(value = "状态：0-已取消，1-正常")
    private Integer status = 1;

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
