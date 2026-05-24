package com.bikesystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 骑行记录实体类
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@Data
@TableName("ride_record")
@Schema(description = "骑行记录")
public class RideRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "记录ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "车辆ID")
    private Long bikeId;

    @Schema(description = "车辆编号")
    private String bikeNo;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "起点纬度")
    private BigDecimal startLatitude;

    @Schema(description = "起点经度")
    private BigDecimal startLongitude;

    @Schema(description = "起点地址")
    private String startAddress;

    @Schema(description = "终点纬度")
    private BigDecimal endLatitude;

    @Schema(description = "终点经度")
    private BigDecimal endLongitude;

    @Schema(description = "终点地址")
    private String endAddress;

    @Schema(description = "骑行时长(分钟)")
    private Integer durationMinutes;

    @Schema(description = "骑行距离(公里)")
    private BigDecimal distanceKm;

    @Schema(description = "基础费用")
    private BigDecimal basePrice;

    @Schema(description = "时长费用")
    private BigDecimal timePrice;

    @Schema(description = "高峰附加费")
    private BigDecimal peakSurcharge;

    @Schema(description = "优惠金额")
    private BigDecimal discountAmount;

    @Schema(description = "使用的优惠券ID")
    private Long couponId;

    @Schema(description = "总费用")
    private BigDecimal totalAmount;

    @Schema(description = "实付金额")
    private BigDecimal actualAmount;

    @Schema(description = "支付状态:UNPAID-未支付,PAID-已支付,REFUNDED-已退款,PARTIAL_REFUND-部分退款")
    private String paymentStatus;

    @Schema(description = "支付方式:BALANCE-余额,WECHAT-微信,ALIPAY-支付宝")
    private String paymentMethod;

    @Schema(description = "支付时间")
    private LocalDateTime paymentTime;

    @Schema(description = "是否高峰时段")
    private Integer isPeakHour;

    @Schema(description = "开锁方式:QRCODE-扫码,BLUETOOTH-蓝牙")
    private String unlockType;

    @Schema(description = "开锁是否成功")
    private Integer unlockSuccess;

    @Schema(description = "失败原因")
    private String failReason;

    @Schema(description = "状态:ONGOING-进行中,COMPLETED-已完成,CANCELLED-已取消")
    private String status;

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
