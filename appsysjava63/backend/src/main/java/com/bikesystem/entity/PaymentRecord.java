package com.bikesystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付记录实体类
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@Data
@TableName("payment_record")
@Schema(description = "支付记录")
public class PaymentRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "支付ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "关联业务ID")
    private Long relatedId;

    @Schema(description = "关联类型:RIDE-骑行,DEPOSIT-押金,RECHARGE-充值")
    private String relatedType;

    @Schema(description = "支付类型:PAY-支付,REFUND-退款")
    private String paymentType;

    @Schema(description = "金额")
    private BigDecimal amount;

    @Schema(description = "支付方式:BALANCE-余额,WECHAT-微信,ALIPAY-支付宝")
    private String paymentMethod;

    @Schema(description = "第三方支付单号")
    private String thirdPartyNo;

    @Schema(description = "状态:PENDING-待支付,SUCCESS-成功,FAILED-失败,CANCELLED-已取消")
    private String status;

    @Schema(description = "支付完成时间")
    private LocalDateTime payTime;

    @Schema(description = "失败原因")
    private String failReason;

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
