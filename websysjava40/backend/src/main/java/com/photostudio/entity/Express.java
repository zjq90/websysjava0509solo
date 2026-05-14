package com.photostudio.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 快递实体类
 * 用于管理订单快递配送信息
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "express")
@Schema(description = "快递信息")
public class Express {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "快递ID", example = "1")
    private Long id;

    @NotNull(message = "订单不能为空")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @Schema(description = "订单信息")
    private Order order;

    @NotBlank(message = "快递单号不能为空")
    @Column(nullable = false, length = 50)
    @Schema(description = "快递单号", example = "SF1234567890")
    private String trackingNo;

    @NotNull(message = "物流公司不能为空")
    @Column(nullable = false, length = 20)
    @Schema(description = "物流公司", example = "SF_EXPRESS")
    @Enumerated(EnumType.STRING)
    private LogisticsCompany company;

    @Column(length = 20)
    @Schema(description = "收件人姓名", example = "李四")
    private String receiverName;

    @Column(length = 50)
    @Schema(description = "收件人电话", example = "13900139000")
    private String receiverPhone;

    @Column(length = 200)
    @Schema(description = "收件地址", example = "北京市朝阳区XX路XX号")
    private String receiverAddress;

    @Column(nullable = false, length = 20)
    @Schema(description = "物流状态", example = "IN_TRANSIT")
    @Enumerated(EnumType.STRING)
    private ExpressStatus status = ExpressStatus.IN_TRANSIT;

    @Column(length = 1000)
    @Schema(description = "物流轨迹信息", example = "已揽收 -> 运输中 -> 派送中")
    private String trackingInfo;

    @Column
    @Schema(description = "发货时间")
    private LocalDateTime shipTime;

    @Column
    @Schema(description = "签收时间")
    private LocalDateTime signTime;

    @Column(length = 500)
    @Schema(description = "备注")
    private String remark;

    @Column
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }

    /**
     * 物流公司枚举
     */
    public enum LogisticsCompany {
        SF_EXPRESS("顺丰速运"),
        JD_EXPRESS("京东物流"),
        YTO_EXPRESS("圆通速递"),
        ZTO_EXPRESS("中通快递"),
        STO_EXPRESS("申通快递"),
        YUNDA_EXPRESS("韵达快递");

        private final String description;

        LogisticsCompany(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 物流状态枚举
     */
    public enum ExpressStatus {
        PENDING("待发货"),
        IN_TRANSIT("运输中"),
        DELIVERING("派送中"),
        SIGNED("已签收"),
        FAILED("签收失败");

        private final String description;

        ExpressStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
