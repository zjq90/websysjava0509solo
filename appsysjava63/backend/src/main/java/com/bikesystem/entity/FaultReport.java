package com.bikesystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 故障上报实体类
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@Data
@TableName("fault_report")
@Schema(description = "故障上报")
public class FaultReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "上报ID")
    private Long id;

    @Schema(description = "上报用户ID")
    private Long userId;

    @Schema(description = "车辆ID")
    private Long bikeId;

    @Schema(description = "车辆编号")
    private String bikeNo;

    @Schema(description = "故障类型:BRAKE-刹车失灵,CHAIN-链条脱落,TIRE-轮胎问题,LOCK-锁故障,ELECTRIC-电气故障,OTHER-其他")
    private String faultType;

    @Schema(description = "故障描述")
    private String faultDescription;

    @Schema(description = "故障图片URLs")
    private String imageUrls;

    @Schema(description = "上报位置纬度")
    private BigDecimal latitude;

    @Schema(description = "上报位置经度")
    private BigDecimal longitude;

    @Schema(description = "状态:PENDING-待处理,PROCESSING-处理中,RESOLVED-已解决,REJECTED-已驳回")
    private String status;

    @Schema(description = "指派运维人员ID")
    private Long maintenanceId;

    @Schema(description = "奖励优惠券ID")
    private Long rewardCouponId;

    @Schema(description = "奖励金额")
    private BigDecimal rewardAmount;

    @Schema(description = "处理备注")
    private String handleRemark;

    @Schema(description = "处理时间")
    private LocalDateTime handleTime;

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
