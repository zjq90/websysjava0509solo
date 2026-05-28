package com.club.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 学校实体类
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "t_school")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "学校信息")
public class School extends BaseEntity {

    @Schema(description = "学校名称", example = "清华大学")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Schema(description = "学校简称", example = "清华")
    @Column(name = "short_name", length = 50)
    private String shortName;

    @Schema(description = "学校Logo", example = "https://example.com/logo.png")
    @Column(name = "logo", length = 500)
    private String logo;

    @Schema(description = "学校地址", example = "北京市海淀区")
    @Column(name = "address", length = 200)
    private String address;

    @Schema(description = "联系电话", example = "010-12345678")
    @Column(name = "phone", length = 20)
    private String phone;

    @Schema(description = "学校简介", example = "清华大学是中国著名高等学府")
    @Column(name = "description", length = 1000)
    private String description;

    @Schema(description = "状态：0-禁用 1-启用", example = "1")
    @Column(name = "status", nullable = false)
    private Integer status = 1;
}
