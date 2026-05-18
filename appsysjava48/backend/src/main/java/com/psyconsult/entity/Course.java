package com.psyconsult.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "course")
@ApiModel(description = "培训课程实体")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "课程ID")
    private Long id;

    @Column(nullable = false, length = 200)
    @ApiModelProperty(value = "课程标题")
    private String title;

    @Column(length = 2000)
    @ApiModelProperty(value = "课程描述")
    private String description;

    @Column(length = 500)
    @ApiModelProperty(value = "课程封面")
    private String coverImage;

    @Column(length = 500)
    @ApiModelProperty(value = "讲师")
    private String instructor;

    @Column(length = 100)
    @ApiModelProperty(value = "课程分类")
    private String category;

    @Column(name = "total_duration")
    @ApiModelProperty(value = "总时长（分钟）")
    private Integer totalDuration;

    @Column(name = "lesson_count")
    @ApiModelProperty(value = "课时数")
    private Integer lessonCount;

    @Column(name = "study_count")
    @ApiModelProperty(value = "学习人数")
    private Integer studyCount = 0;

    @Column(length = 500)
    @ApiModelProperty(value = "课程链接")
    private String courseUrl;

    @Column(nullable = false)
    @ApiModelProperty(value = "状态：0-下架，1-上架")
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
