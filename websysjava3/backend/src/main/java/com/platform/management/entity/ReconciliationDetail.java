package com.platform.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 对账差异明细实体类
 * 
 * @author platform
 * @version 1.0.0
 */
@Data
@TableName("reconciliation_detail")
@Schema(description = "对账差异明细信息")
public class ReconciliationDetail {

    @TableId(type = IdType.AUTO)
    @Schema(description = "明细ID")
    private Long id;

    @Schema(description = "对账ID")
    private Long reconId;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "差异类型: MISSING_SYSTEM/MISSING_THIRD/AMOUNT_DIFF")
    private String diffType;

    @Schema(description = "系统金额")
    private BigDecimal systemAmount;

    @Schema(description = "第三方金额")
    private BigDecimal thirdAmount;

    @Schema(description = "处理状态")
    private String status;

    @Schema(description = "备注")
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableLogic
    @Schema(description = "删除标记")
    private Integer deleted;
}
