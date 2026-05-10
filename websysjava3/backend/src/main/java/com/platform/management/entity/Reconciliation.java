package com.platform.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 对账记录实体类
 * 
 * @author platform
 * @version 1.0.0
 */
@Data
@TableName("reconciliation")
@Schema(description = "对账记录信息")
public class Reconciliation {

    @TableId(type = IdType.AUTO)
    @Schema(description = "对账记录ID")
    private Long id;

    @Schema(description = "对账日期")
    private LocalDate reconDate;

    @Schema(description = "支付渠道")
    private String payChannel;

    @Schema(description = "对账类型: DAILY/MONTHLY")
    private String reconType;

    @Schema(description = "系统金额")
    private BigDecimal systemAmount;

    @Schema(description = "第三方金额")
    private BigDecimal thirdAmount;

    @Schema(description = "差异金额")
    private BigDecimal diffAmount;

    @Schema(description = "系统订单数")
    private Integer systemCount;

    @Schema(description = "第三方订单数")
    private Integer thirdCount;

    @Schema(description = "差异订单数")
    private Integer diffCount;

    @Schema(description = "对账状态: PENDING/MATCHED/UNMATCHED")
    private String status;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "对账时间")
    private LocalDateTime reconTime;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableLogic
    @Schema(description = "删除标记")
    private Integer deleted;
}
