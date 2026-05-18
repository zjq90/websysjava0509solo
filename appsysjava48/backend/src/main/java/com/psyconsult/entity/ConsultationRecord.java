package com.psyconsult.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "consultation_record")
@ApiModel(description = "咨询记录实体")
public class ConsultationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "记录ID")
    private Long id;

    @Column(name = "appointment_id", nullable = false)
    @ApiModelProperty(value = "关联预约ID")
    private Long appointmentId;

    @Column(name = "user_id", nullable = false)
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @Column(name = "counselor_id", nullable = false)
    @ApiModelProperty(value = "咨询师ID")
    private Long counselorId;

    @Column(name = "chief_complaint", length = 2000)
    @ApiModelProperty(value = "用户主诉")
    private String chiefComplaint;

    @Column(name = "intervention_measures", length = 4000)
    @ApiModelProperty(value = "干预措施")
    private String interventionMeasures;

    @Column(name = "follow_up_suggestions", length = 4000)
    @ApiModelProperty(value = "后续建议")
    private String followUpSuggestions;

    @Column(name = "consultation_summary", length = 4000)
    @ApiModelProperty(value = "咨询摘要")
    private String consultationSummary;

    @Column(length = 2000)
    @ApiModelProperty(value = "标签（逗号分隔）")
    private String tags;

    @Column(name = "mood_rating")
    @ApiModelProperty(value = "情绪评分（1-10）")
    private Integer moodRating;

    @Column(name = "risk_level", length = 20)
    @ApiModelProperty(value = "风险等级：low-低，medium-中，high-高")
    private String riskLevel = "low";

    @Column(name = "next_appointment")
    @ApiModelProperty(value = "下次预约时间")
    private LocalDateTime nextAppointment;

    @Column(length = 500)
    @ApiModelProperty(value = "咨询师备注")
    private String counselorNotes;

    @Column(nullable = false)
    @ApiModelProperty(value = "状态：0-草稿，1-已完成")
    private Integer status = 0;

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
