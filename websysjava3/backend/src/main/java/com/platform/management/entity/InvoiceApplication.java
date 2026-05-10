package com.platform.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 发票申请实体类
 * 
 * @author platform
 * @version 1.0.0
 */
@Data
@TableName("invoice_application")
@Schema(description = "发票申请信息")
public class InvoiceApplication {

    @TableId(type = IdType.AUTO)
    @Schema(description = "发票申请ID")
    private Long id;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "发票类型: PERSONAL/ENTERPRISE")
    private String invoiceType;

    @Schema(description = "发票抬头")
    private String invoiceTitle;

    @Schema(description = "税号")
    private String taxNo;

    @Schema(description = "发票内容")
    private String invoiceContent;

    @Schema(description = "开票金额")
    private BigDecimal amount;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "电话")
    private String phone;

    @Schema(description = "状态: PENDING/APPROVED/REJECTED/ISSUED")
    private String status;

    @Schema(description = "发票号码")
    private String invoiceNo;

    @Schema(description = "发票代码")
    private String invoiceCode;

    @Schema(description = "开票时间")
    private LocalDateTime issueTime;

    @Schema(description = "拒绝原因")
    private String rejectReason;

    @Schema(description = "审核人")
    private String reviewer;

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
