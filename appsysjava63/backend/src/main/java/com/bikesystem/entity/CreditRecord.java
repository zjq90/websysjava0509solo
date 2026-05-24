package com.bikesystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 信用分记录实体类
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@Data
@TableName("credit_record")
@Schema(description = "信用分记录")
public class CreditRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "记录ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "变动类型:DEDUCT-扣分,REWARD-加分")
    private String changeType;

    @Schema(description = "变动分数")
    private Integer scoreChange;

    @Schema(description = "变动原因")
    private String reason;

    @Schema(description = "关联记录ID")
    private Long relatedId;

    @Schema(description = "关联类型:RIDE-骑行,FAULT-故障上报,VIOLATION-违规")
    private String relatedType;

    @Schema(description = "变动前信用分")
    private Integer beforeScore;

    @Schema(description = "变动后信用分")
    private Integer afterScore;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "逻辑删除")
    @TableLogic
    private Integer deleted;
}
