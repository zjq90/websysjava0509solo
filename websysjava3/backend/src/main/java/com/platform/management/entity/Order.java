package com.platform.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 * 
 * @author platform
 * @version 1.0.0
 */
@Data
@TableName("orders")
@Schema(description = "订单信息")
public class Order {

    @TableId(type = IdType.AUTO)
    @Schema(description = "订单ID")
    private Long id;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "订单金额")
    private BigDecimal amount;

    @Schema(description = "支付渠道: WECHAT/ALIPAY/UNIONPAY")
    private String payChannel;

    @Schema(description = "支付状态: PENDING/SUCCESS/FAILED/REFUNDED")
    private String payStatus;

    @Schema(description = "出货状态: PENDING/SHIPPED/FAILED")
    private String shipStatus;

    @Schema(description = "订单状态: NORMAL/EXCEPTION")
    private String orderStatus;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "支付时间")
    private LocalDateTime payTime;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @TableLogic
    @Schema(description = "删除标记")
    private Integer deleted;
}
