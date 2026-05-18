package com.flowerstore.backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 优惠券实体类
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coupon")
@Schema(description = "优惠券")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "优惠券ID")
    private Long id;

    @Column(length = 100)
    @Schema(description = "优惠券名称")
    private String name;

    @Column(length = 200)
    @Schema(description = "优惠券描述")
    private String description;

    @Schema(description = "优惠券类型：1-满减券，2-折扣券，3-免邮券")
    private Integer type;

    @Schema(description = "面值金额（分），满减券使用")
    private Long value;

    @Schema(description = "折扣率，85表示85折，折扣券使用")
    private Integer discountRate;

    @Schema(description = "最低消费金额（分）")
    private Long minAmount;

    @Schema(description = "发放数量")
    private Integer totalCount;

    @Schema(description = "已领取数量")
    private Integer receiveCount;

    @Schema(description = "已使用数量")
    private Integer usedCount;

    @Schema(description = "每人限领数量")
    private Integer perLimit;

    @Schema(description = "是否可用积分兑换：0-否，1-是")
    private Integer pointExchange;

    @Schema(description = "兑换所需积分")
    private Integer exchangePoints;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "有效开始时间")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "有效结束时间")
    private LocalDateTime endTime;

    @Schema(description = "状态：0-禁用，1-启用")
    private Integer status;

    @Schema(description = "排序")
    private Integer sortOrder;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        if (status == null) status = 1;
        if (receiveCount == null) receiveCount = 0;
        if (usedCount == null) usedCount = 0;
        if (pointExchange == null) pointExchange = 0;
        if (sortOrder == null) sortOrder = 0;
    }
}
