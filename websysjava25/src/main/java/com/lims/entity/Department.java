package com.lims.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 科室实体类
 * 存储临床科室、检验科室、检查科室等信息
 *
 * @author LIMS Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "department")
@Schema(description = "科室信息")
public class Department {

    /**
     * 科室ID，主键自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "科室ID", example = "1")
    private Long id;

    /**
     * 科室编号，唯一标识
     */
    @Column(unique = true, nullable = false, length = 50)
    @Schema(description = "科室编号", example = "DEPT001")
    private String deptCode;

    /**
     * 科室名称
     */
    @Column(nullable = false, length = 100)
    @Schema(description = "科室名称", example = "检验科")
    private String deptName;

    /**
     * 科室类型：CLINICAL-临床科室，LABORATORY-检验科室，EXAMINATION-检查科室
     */
    @Column(nullable = false, length = 50)
    @Schema(description = "科室类型", example = "LABORATORY")
    private String deptType;

    /**
     * 科室负责人
     */
    @Column(length = 50)
    @Schema(description = "科室负责人", example = "张医生")
    private String director;

    /**
     * 联系电话
     */
    @Column(length = 20)
    @Schema(description = "联系电话", example = "010-12345678")
    private String phone;

    /**
     * 科室描述
     */
    @Column(length = 500)
    @Schema(description = "科室描述", example = "负责各类检验项目")
    private String description;

    /**
     * 状态：ACTIVE-启用，INACTIVE-停用
     */
    @Column(nullable = false, length = 20)
    @Schema(description = "状态", example = "ACTIVE")
    private String status;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        status = "ACTIVE";
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
