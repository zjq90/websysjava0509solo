package com.flowerstore.backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 订单实体类
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_order")
@Schema(description = "订单信息")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "订单ID")
    private Long id;

    @Column(unique = true, length = 50)
    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "订单总金额（分）")
    private Long totalAmount;

    @Schema(description = "运费（分）")
    private Long freightAmount;

    @Schema(description = "优惠金额（分）")
    private Long discountAmount;

    @Schema(description = "会员折扣金额（分）")
    private Long memberDiscountAmount;

    @Schema(description = "积分抵扣金额（分）")
    private Long pointDiscountAmount;

    @Schema(description = "实付金额（分）")
    private Long payAmount;

    @Schema(description = "获得积分")
    private Integer earnedPoints;

    @Schema(description = "使用积分")
    private Integer usedPoints;

    @Schema(description = "优惠券ID")
    private Long couponId;

    @Column(length = 20)
    @Schema(description = "支付方式：alipay-支付宝，wechat-微信支付")
    private String payType;

    @Schema(description = "支付状态：0-未支付，1-已支付，2-已退款")
    private Integer payStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "支付时间")
    private LocalDateTime payTime;

    @Schema(description = "订单状态：0-待付款，1-待发货，2-已发货，3-已完成，4-已取消，5-已退款")
    private Integer status;

    @Column(length = 20)
    @Schema(description = "收货人姓名")
    private String receiverName;

    @Column(length = 20)
    @Schema(description = "收货人电话")
    private String receiverPhone;

    @Column(length = 100)
    @Schema(description = "收货人省")
    private String receiverProvince;

    @Column(length = 100)
    @Schema(description = "收货人市")
    private String receiverCity;

    @Column(length = 100)
    @Schema(description = "收货人区/县")
    private String receiverDistrict;

    @Column(length = 500)
    @Schema(description = "收货人详细地址")
    private String receiverAddress;

    @Column(length = 500)
    @Schema(description = "订单备注")
    private String remark;

    @Schema(description = "是否使用生日折扣：0-否，1-是")
    private Integer birthdayDiscount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "发货时间")
    private LocalDateTime deliveryTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "确认收货时间")
    private LocalDateTime confirmTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "取消时间")
    private LocalDateTime cancelTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "完成时间")
    private LocalDateTime completeTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (status == null) status = 0;
        if (payStatus == null) payStatus = 0;
        if (earnedPoints == null) earnedPoints = 0;
        if (usedPoints == null) usedPoints = 0;
        if (birthdayDiscount == null) birthdayDiscount = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
