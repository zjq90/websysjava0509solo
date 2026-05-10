package com.platform.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 销售记录实体类
 * 
 * @author platform
 * @version 1.0.0
 */
@Data
@TableName("sales_record")
@Schema(description = "销售记录信息")
public class SalesRecord {

    @TableId(type = IdType.AUTO)
    @Schema(description = "销售记录ID")
    private Long id;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "销售金额")
    private BigDecimal amount;

    @Schema(description = "支付渠道")
    private String payChannel;

    @Schema(description = "销售日期")
    private LocalDate saleDate;

    @Schema(description = "销售时间")
    private LocalDateTime saleTime;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableLogic
    @Schema(description = "删除标记")
    private Integer deleted;
}
