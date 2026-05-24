package com.bikesystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 预约记录实体类
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@Data
@TableName("reservation")
@Schema(description = "预约记录")
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "预约ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "车辆ID")
    private Long bikeId;

    @Schema(description = "车辆编号")
    private String bikeNo;

    @Schema(description = "预约时间")
    private LocalDateTime reserveTime;

    @Schema(description = "过期时间")
    private LocalDateTime expireTime;

    @Schema(description = "取消时间")
    private LocalDateTime cancelTime;

    @Schema(description = "状态:ACTIVE-有效,EXPIRED-已过期,CANCELLED-已取消,USED-已使用")
    private String status;

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
