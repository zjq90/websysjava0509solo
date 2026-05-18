package com.psyconsult.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "supervision")
@ApiModel(description = "督导预约实体")
public class Supervision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "督导ID")
    private Long id;

    @Column(name = "counselor_id", nullable = false)
    @ApiModelProperty(value = "咨询师ID")
    private Long counselorId;

    @Column(name = "supervisor_id", nullable = false)
    @ApiModelProperty(value = "督导ID")
    private Long supervisorId;

    @Column(nullable = false)
    @ApiModelProperty(value = "预约时间")
    private LocalDateTime appointmentTime;

    @Column(name = "duration_minutes")
    @ApiModelProperty(value = "时长（分钟）")
    private Integer durationMinutes = 60;

    @Column(length = 2000)
    @ApiModelProperty(value = "督导主题")
    private String topic;

    @Column(length = 4000)
    @ApiModelProperty(value = "案例描述")
    private String caseDescription;

    @Column(length = 4000)
    @ApiModelProperty(value = "督导记录")
    private String supervisionNotes;

    @Column(length = 20)
    @ApiModelProperty(value = "状态：pending-待确认，confirmed-已确认，completed-已完成，cancelled-已取消")
    private String status = "pending";

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
