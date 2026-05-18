package com.psyconsult.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "case_tag")
@ApiModel(description = "案例标签实体")
public class CaseTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "标签ID")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @ApiModelProperty(value = "标签名称")
    private String name;

    @Column(length = 50)
    @ApiModelProperty(value = "标签分类：emotion-情绪，relationship-人际关系，work-工作压力，other-其他")
    private String category;

    @Column(length = 200)
    @ApiModelProperty(value = "标签描述")
    private String description;

    @Column(name = "use_count")
    @ApiModelProperty(value = "使用次数")
    private Integer useCount = 0;

    @Column(nullable = false)
    @ApiModelProperty(value = "状态：0-禁用，1-启用")
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
