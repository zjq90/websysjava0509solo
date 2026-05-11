package com.hospital.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 科室实体类
 * 医院科室管理
 * 
 * @author hospital
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "hos_department")
@Schema(description = "科室信息")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "科室ID")
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    @Schema(description = "科室编码")
    private String deptCode;

    @Column(unique = true, nullable = false, length = 50)
    @Schema(description = "科室名称")
    private String deptName;

    @Column(length = 200)
    @Schema(description = "科室描述")
    private String description;

    @Column(length = 20)
    @Schema(description = "科室类型：内科 外科 其他")
    private String deptType;

    @Column
    @Schema(description = "总床位数")
    private Integer totalBeds = 0;

    @Column
    @Schema(description = "可用床位数")
    private Integer availableBeds = 0;

    @Column(length = 50)
    @Schema(description = "科室负责人")
    private String headDoctor;

    @Column(length = 20)
    @Schema(description = "联系电话")
    private String phone;

    @Column(length = 100)
    @Schema(description = "科室位置")
    private String location;

    @Column(nullable = false)
    @Schema(description = "状态：0停用 1启用")
    private Integer status = 1;

    @Column
    @Schema(description = "排序")
    private Integer sortOrder = 0;

    @Column(nullable = false, updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}
