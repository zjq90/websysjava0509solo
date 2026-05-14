package com.photostudio.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 员工实体类
 * 用于存储摄影师、化妆师、选片师等工作人员信息
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "employee")
@Schema(description = "员工信息")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "员工ID", example = "1")
    private Long id;

    @NotBlank(message = "员工姓名不能为空")
    @Column(nullable = false, length = 50)
    @Schema(description = "员工姓名", example = "张三")
    private String name;

    @NotBlank(message = "员工工号不能为空")
    @Column(unique = true, nullable = false, length = 20)
    @Schema(description = "员工工号", example = "EMP001")
    private String employeeNo;

    @NotNull(message = "员工职位不能为空")
    @Column(nullable = false, length = 20)
    @Schema(description = "职位", example = "PHOTOGRAPHER")
    @Enumerated(EnumType.STRING)
    private EmployeePosition position;

    @Column(length = 20)
    @Schema(description = "联系电话", example = "13800138000")
    private String phone;

    @Column(length = 100)
    @Schema(description = "邮箱", example = "zhangsan@photostudio.com")
    private String email;

    @Column(nullable = false)
    @Schema(description = "是否在职", example = "true")
    private Boolean active = true;

    @Column(length = 500)
    @Schema(description = "备注")
    private String remark;

    /**
     * 员工职位枚举
     */
    public enum EmployeePosition {
        PHOTOGRAPHER("摄影师"),
        MAKEUP_ARTIST("化妆师"),
        FILM_SELECTOR("选片师"),
        ASSISTANT("助理"),
        MANAGER("经理");

        private final String description;

        EmployeePosition(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
