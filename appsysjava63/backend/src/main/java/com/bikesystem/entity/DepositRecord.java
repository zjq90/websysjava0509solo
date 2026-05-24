package com.bikesystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 押金记录实体类
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@Data
@TableName("deposit_record")
@Schema(description = "押金记录")
public class DepositRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "记录ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "类型:PAY-缴纳,REFUND-退还,EXEMPT-免押金授权")
    private String recordType;

    @Schema(description = "金额")
    private BigDecimal amount;

    @Schema(description = "支付方式")
    private String paymentMethod;

    @Schema(description = "支付单号")
    private String paymentNo;

    @Schema(description = "信用授权来源:ZHIMA-芝麻信用")
    private String creditAuthSource;

    @Schema(description = "授权时信用分")
    private Integer creditScore;

    @Schema(description = "状态:SUCCESS-成功,FAILED-失败,PROCESSING-处理中")
    private String status;

    @Schema(description = "备注")
    private String remark;

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
