package com.referee.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.persistence.*;

/**
 * 运动员实体类
 * 存储运动员的详细信息
 *
 * @author Referee System
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "athlete")
@Schema(description = "运动员实体")
public class Athlete {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "运动员ID", example = "1")
    private Long id;

    /**
     * 运动员编号
     */
    @Column(nullable = false, unique = true, length = 50)
    @Schema(description = "运动员编号", example = "A001", required = true)
    private String athleteNo;

    /**
     * 姓名
     */
    @Column(nullable = false, length = 50)
    @Schema(description = "姓名", example = "王强", required = true)
    private String name;

    /**
     * 性别：男/女
     */
    @Column(length = 10)
    @Schema(description = "性别", example = "男")
    private String gender;

    /**
     * 年龄
     */
    @Column
    @Schema(description = "年龄", example = "20")
    private Integer age;

    /**
     * 身份证号
     */
    @Column(length = 20)
    @Schema(description = "身份证号", example = "110101200401011234")
    private String idCard;

    /**
     * 学校/单位
     */
    @Column(length = 100)
    @Schema(description = "学校/单位", example = "北京大学")
    private String school;

    /**
     * 参赛项目
     */
    @Column(length = 100)
    @Schema(description = "参赛项目", example = "100米短跑")
    private String event;

    /**
     * 联系电话
     */
    @Column(length = 20)
    @Schema(description = "联系电话", example = "13700137000")
    private String phone;

    /**
     * 邮箱
     */
    @Column(length = 100)
    @Schema(description = "邮箱", example = "athlete@example.com")
    private String email;

    /**
     * 评价
     */
    @Column(length = 1000)
    @Schema(description = "评价", example = "表现优秀，技术娴熟")
    private String evaluation;

    /**
     * 状态：1-正常, 0-停用
     */
    @Column(nullable = false)
    @Schema(description = "状态：1-正常, 0-停用", example = "1")
    private Integer status = 1;

    /**
     * 备注
     */
    @Column(length = 500)
    @Schema(description = "备注", example = "种子选手")
    private String remark;
}
