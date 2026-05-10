package com.platform.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 异常订单实体类
 * 
 * @author platform
 * @version 1.0.0
 */
@Data
@TableName("exception_order")
@Schema(description = "异常订单信息")
public class ExceptionOrder {

    @TableId(type = IdType.AUTO)
    @Schema(description = "异常订单ID")
    private Long id;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "异常类型: PAY_SUCCESS_NO_SHIP/SHIP_FAILED/DUPLICATE_PAY/OTHER")
    private String exceptionType;

    @Schema(description = "异常描述")
    private String exceptionDesc;

    @Schema(description = "处理状态: PENDING/PROCESSING/RESOLVED")
    private String handleStatus;

    @Schema(description = "处理方式: REFUND/RESHIP/OTHER")
    private String handleType;

    @Schema(description = "处理备注")
    private String handleRemark;

    @Schema(description = "处理时间")
    private LocalDateTime handleTime;

    @Schema(description = "处理人")
    private String handler;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @TableLogic
    @Schema(description = "删除标记")
    private Integer deleted;

    @TableField(exist = false)
    @Schema(description = "关联订单信息")
    private Order order;
}
