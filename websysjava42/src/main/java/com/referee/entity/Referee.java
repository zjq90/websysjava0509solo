package com.referee.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.persistence.*;

/**
 * 裁判实体类
 * 存储裁判的详细信息
 *
 * @author Referee System
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "referee")
@Schema(description = "裁判实体")
public class Referee {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "裁判ID", example = "1")
    private Long id;

    /**
     * 裁判编号
     */
    @Column(nullable = false, unique = true, length = 50)
    @Schema(description = "裁判编号", example = "R001", required = true)
    private String refereeNo;

    /**
     * 姓名
     */
    @Column(nullable = false, length = 50)
    @Schema(description = "姓名", example = "李明", required = true)
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
    @Schema(description = "年龄", example = "35")
    private Integer age;

    /**
     * 身份证号
     */
    @Column(length = 20)
    @Schema(description = "身份证号", example = "110101199001011234")
    private String idCard;

    /**
     * 裁判等级：国家级/一级/二级/三级
     */
    @Column(length = 20)
    @Schema(description = "裁判等级", example = "一级")
    private String level;

    /**
     * 所属单位
     */
    @Column(length = 100)
    @Schema(description = "所属单位", example = "北京市体育局")
    private String organization;

    /**
     * 专业领域
     */
    @Column(length = 100)
    @Schema(description = "专业领域", example = "田径")
    private String specialty;

    /**
     * 联系电话
     */
    @Column(length = 20)
    @Schema(description = "联系电话", example = "13900139000")
    private String phone;

    /**
     * 邮箱
     */
    @Column(length = 100)
    @Schema(description = "邮箱", example = "referee@example.com")
    private String email;

    /**
     * 是否为裁判长：1-是, 0-否
     */
    @Column(nullable = false)
    @Schema(description = "是否为裁判长：1-是, 0-否", example = "0")
    private Integer isChief = 0;

    /**
     * 状态：1-在职, 0-离职
     */
    @Column(nullable = false)
    @Schema(description = "状态：1-在职, 0-离职", example = "1")
    private Integer status = 1;

    /**
     * 备注
     */
    @Column(length = 500)
    @Schema(description = "备注", example = "资深裁判")
    private String remark;
}
