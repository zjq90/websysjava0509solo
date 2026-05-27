package com.club.entity;

import com.club.enums.ClubStatus;
import com.club.enums.ClubType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 社团实体类
 * 存储社团的基本信息
 *
 * @author Club Management System
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "club")
@Schema(description = "社团信息")
public class Club extends BaseEntity {

    /**
     * 社团名称
     */
    @Column(name = "name", nullable = false, unique = true, length = 100)
    @Schema(description = "社团名称", example = "计算机协会")
    private String name;

    /**
     * 社团简称
     */
    @Column(name = "short_name", length = 50)
    @Schema(description = "社团简称", example = "计协")
    private String shortName;

    /**
     * 社团类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    @Schema(description = "社团类型", example = "ACADEMIC")
    private ClubType type;

    /**
     * 所属院系
     */
    @Column(name = "department", length = 100)
    @Schema(description = "所属院系", example = "计算机学院")
    private String department;

    /**
     * 社团状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    @Schema(description = "社团状态", example = "NORMAL")
    private ClubStatus status = ClubStatus.NORMAL;

    /**
     * 社团简介
     */
    @Column(name = "description", length = 1000)
    @Schema(description = "社团简介", example = "计算机协会成立于2005年...")
    private String description;

    /**
     * 社团章程
     */
    @Column(name = "constitution", columnDefinition = "TEXT")
    @Schema(description = "社团章程", example = "第一章 总则...")
    private String constitution;

    /**
     * 成立日期
     */
    @Column(name = "found_date")
    @Schema(description = "成立日期", example = "2020-09-01")
    private LocalDate foundDate;

    /**
     * 负责人姓名
     */
    @Column(name = "leader_name", length = 50)
    @Schema(description = "负责人姓名", example = "李四")
    private String leaderName;

    /**
     * 负责人联系电话
     */
    @Column(name = "leader_phone", length = 20)
    @Schema(description = "负责人联系电话", example = "13800138001")
    private String leaderPhone;

    /**
     * 指导老师
     */
    @Column(name = "advisor", length = 50)
    @Schema(description = "指导老师", example = "王教授")
    private String advisor;

    /**
     * 成员总数
     */
    @Column(name = "member_count", nullable = false)
    @Schema(description = "成员总数", example = "120")
    private Integer memberCount = 0;

    /**
     * 社团经费余额
     */
    @Column(name = "fund_balance", precision = 10, scale = 2)
    @Schema(description = "经费余额", example = "5000.00")
    private BigDecimal fundBalance = BigDecimal.ZERO;

    /**
     * 年度活动次数
     */
    @Column(name = "annual_activity_count")
    @Schema(description = "年度活动次数", example = "15")
    private Integer annualActivityCount = 0;

    /**
     * 是否完成年度注册
     */
    @Column(name = "annual_registered")
    @Schema(description = "是否完成年度注册", example = "true")
    private Boolean annualRegistered = false;

    /**
     * 注册年份
     */
    @Column(name = "register_year")
    @Schema(description = "注册年份", example = "2024")
    private Integer registerYear;

    /**
     * 备注
     */
    @Column(name = "remark", length = 500)
    @Schema(description = "备注")
    private String remark;
}
