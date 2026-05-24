package com.bikesystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 开锁请求DTO
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@Data
@Schema(description = "开锁请求")
public class UnlockRequest {

    @Schema(description = "车辆ID(与二维码二选一)")
    private Long bikeId;

    @Schema(description = "二维码内容(与车辆ID二选一)")
    private String qrCode;

    @Schema(description = "蓝牙MAC地址(蓝牙开锁时使用)")
    private String bluetoothMac;

    @Schema(description = "开锁方式:QRCODE-扫码,BLUETOOTH-蓝牙", required = true)
    @NotNull(message = "开锁方式不能为空")
    private String unlockType;

    @Schema(description = "当前纬度", required = true)
    @NotNull(message = "纬度不能为空")
    private BigDecimal latitude;

    @Schema(description = "当前经度", required = true)
    @NotNull(message = "经度不能为空")
    private BigDecimal longitude;

    @Schema(description = "当前位置地址")
    private String address;
}
