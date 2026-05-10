package com.platform.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付记录实体类
 * 
 * @author platform
 * @version 1.0.0
 */
@Data
@TableName("payment_record")
@Schema(description = "支付记录信息")
public class PaymentRecord {

    @TableId(type = IdType.AUTO)
    @Schema(description = "支付记录ID")
    private Long id;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "支付渠道")
    private String payChannel;

    @Schema(description = "第三方交易号")
    private String tradeNo;

    @Schema(description = "支付金额")
    private BigDecimal amount;

    @Schema(description = "支付状态")
    private String payStatus;

    @Schema(description = "支付时间")
    private LocalDateTime payTime;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableLogic
    @Schema(description = "删除标记")
    private Integer deleted;
}
