package com.bikesystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 车辆实体类
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@Data
@TableName("bike")
@Schema(description = "车辆信息")
public class Bike implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "车辆ID")
    private Long id;

    @Schema(description = "车辆编号")
    private String bikeNo;

    @Schema(description = "车型:STANDARD-普通单车,ELECTRIC-电动车,ASSIST-助力车")
    private String bikeType;

    @Schema(description = "二维码内容")
    private String qrCode;

    @Schema(description = "蓝牙MAC地址")
    private String bluetoothMac;

    @Schema(description = "电量百分比")
    private Integer batteryLevel;

    @Schema(description = "续航里程(公里)")
    private BigDecimal rangeKm;

    @Schema(description = "当前纬度")
    private BigDecimal latitude;

    @Schema(description = "当前经度")
    private BigDecimal longitude;

    @Schema(description = "位置地址")
    private String locationAddress;

    @Schema(description = "状态:AVAILABLE-可用,IN_USE-使用中,RESERVED-已预约,MAINTENANCE-维护中,FAULTY-故障,LOST-丢失")
    private String status;

    @Schema(description = "最后维护时间")
    private LocalDateTime lastMaintenanceTime;

    @Schema(description = "总骑行次数")
    private Integer totalRides;

    @Schema(description = "总骑行里程")
    private BigDecimal totalKm;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Schema(description = "逻辑删除")
    @TableLogic
    private Integer deleted;
}
