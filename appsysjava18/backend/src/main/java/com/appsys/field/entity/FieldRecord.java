package com.appsys.field.entity;

import com.appsys.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 田间记录实体类
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "field_record", indexes = {
    @Index(name = "idx_field_name", columnList = "field_name"),
    @Index(name = "idx_record_date", columnList = "record_date"),
    @Index(name = "idx_deleted", columnList = "deleted")
})
@Schema(description = "田间记录")
public class FieldRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "地块名称")
    @Column(name = "field_name", nullable = false, length = 100)
    private String fieldName;

    @Schema(description = "地块位置")
    @Column(name = "location", length = 255)
    private String location;

    @Schema(description = "地块面积（亩）")
    @Column(name = "area", precision = 10, scale = 2)
    private BigDecimal area;

    @Schema(description = "种植作物")
    @Column(name = "crop_name", length = 100)
    private String cropName;

    @Schema(description = "品种")
    @Column(name = "variety", length = 100)
    private String variety;

    @Schema(description = "记录日期")
    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Schema(description = "天气情况")
    @Column(name = "weather", length = 50)
    private String weather;

    @Schema(description = "温度（℃）")
    @Column(name = "temperature", precision = 5, scale = 1)
    private BigDecimal temperature;

    @Schema(description = "湿度（%）")
    @Column(name = "humidity", precision = 5, scale = 1)
    private BigDecimal humidity;

    @Schema(description = "土壤情况")
    @Column(name = "soil_condition", length = 255)
    private String soilCondition;

    @Schema(description = "病虫害情况")
    @Column(name = "pest_status", length = 500)
    private String pestStatus;

    @Schema(description = "施肥情况")
    @Column(name = "fertilization", length = 500)
    private String fertilization;

    @Schema(description = "灌溉情况")
    @Column(name = "irrigation", length = 500)
    private String irrigation;

    @Schema(description = "生长情况描述")
    @Column(name = "growth_status", length = 1000)
    private String growthStatus;

    @Schema(description = "操作内容")
    @Column(name = "operation", length = 1000)
    private String operation;

    @Schema(description = "农技员ID")
    @Column(name = "technician_id")
    private Long technicianId;

    @Schema(description = "农技员名称")
    @Column(name = "technician_name", length = 50)
    private String technicianName;

    @Schema(description = "备注")
    @Column(name = "remark", length = 500)
    private String remark;

    @Schema(description = "图片URL（多个用逗号分隔）")
    @Column(name = "images", length = 2000)
    private String images;
}
