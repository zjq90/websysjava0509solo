package com.psyconsult.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "crisis_alert")
@ApiModel(description = "危机预警实体")
public class CrisisAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "预警ID")
    private Long id;

    @Column(name = "record_id")
    @ApiModelProperty(value = "关联咨询记录ID")
    private Long recordId;

    @Column(name = "user_id", nullable = false)
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @Column(name = "counselor_id")
    @ApiModelProperty(value = "咨询师ID")
    private Long counselorId;

    @Column(length = 500)
    @ApiModelProperty(value = "触发关键词")
    private String triggerWords;

    @Column(length = 2000)
    @ApiModelProperty(value = "预警内容")
    private String content;

    @Column(name = "alert_level", length = 20)
    @ApiModelProperty(value = "预警等级：low-低，medium-中，high-高，critical-紧急")
    private String alertLevel = "medium";

    @Column(name = "supervisor_id")
    @ApiModelProperty(value = "督导ID")
    private Long supervisorId;

    @Column(length = 20)
    @ApiModelProperty(value = "处理状态：pending-待处理，processing-处理中，resolved-已解决")
    private String status = "pending";

    @Column(length = 2000)
    @ApiModelProperty(value = "处理结果")
    private String handleResult;

    @Column(name = "handle_time")
    @ApiModelProperty(value = "处理时间")
    private LocalDateTime handleTime;

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
