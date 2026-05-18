package com.flower.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 订单创建DTO
 */
@Data
public class OrderCreateDTO {

    @NotEmpty(message = "购物车商品不能为空")
    private List<Long> cartItemIds;

    @NotBlank(message = "配送类型不能为空")
    private String deliveryType;

    private String deliveryTime;

    @NotBlank(message = "收货人姓名不能为空")
    private String receiverName;

    @NotBlank(message = "收货人电话不能为空")
    private String receiverPhone;

    @NotBlank(message = "收货地址不能为空")
    private String receiverAddress;

    private Long couponId;

    private Integer pointsUsed;

    private String remark;
}