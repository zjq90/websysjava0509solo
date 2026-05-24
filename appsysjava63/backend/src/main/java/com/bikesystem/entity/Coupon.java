package com.bikesystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠券实体类
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@Data
@TableName("coupon")
@Schema(description = "优惠券")
public class Coupon implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "优惠券ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "优惠券模板ID")
    private Long couponTemplateId;

    @Schema(description = "优惠券名称")
    private String couponName;

    @Schema(description = "类型:DISCOUNT-折扣券,FIXED-满减券,FREE-免费骑行券")
    private String couponType;

    @Schema(description = "折扣值")
    private BigDecimal discountValue;

    @Schema(description = "最低使用金额")
    private BigDecimal minAmount;

    @Schema(description = "最大优惠金额")
    private BigDecimal maxDiscount;

    @Schema(description = "发放时间")
    private LocalDateTime issueTime;

    @Schema(description = "过期时间")
    private LocalDateTime expireTime;

    @Schema(description = "使用时间")
    private LocalDateTime useTime;

    @Schema(description = "关联骑行记录ID")
    private Long rideRecordId;

    @Schema(description = "状态:UNUSED-未使用,USED-已使用,EXPIRED-已过期")
    private String status;

    @Schema(description = "来源:REGISTER-注册,FAULT_REPORT-故障上报,ACTIVITY-活动,PURCHASE-购买")
    private String source;

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
