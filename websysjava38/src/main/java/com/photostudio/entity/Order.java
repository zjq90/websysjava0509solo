package com.photostudio.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "orders")
@Schema(description = "订单信息")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "订单ID", example = "1")
    private Long id;

    @Column(name = "order_no", nullable = false, length = 50, unique = true)
    @Schema(description = "订单编号", example = "PS202401010001")
    private String orderNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @Schema(description = "客户ID")
    private Customer customer;

    @Column(name = "customer_name", length = 50)
    @Schema(description = "客户姓名", example = "张三")
    private String customerName;

    @Column(name = "customer_phone", length = 20)
    @Schema(description = "客户电话", example = "13800138000")
    private String customerPhone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id")
    @Schema(description = "套餐ID")
    private Package packageInfo;

    @Column(name = "package_name", length = 100)
    @Schema(description = "套餐名称", example = "轻奢婚纱套餐")
    private String packageName;

    @Column(name = "package_price", precision = 10, scale = 2)
    @Schema(description = "套餐价格", example = "5999.00")
    private BigDecimal packagePrice;

    @Column(name = "add_on_items_json", length = 2000)
    @Schema(description = "加购项JSON")
    private String addOnItemsJson;

    @Column(name = "add_on_total", precision = 10, scale = 2)
    @Schema(description = "加购项总价", example = "1000.00")
    private BigDecimal addOnTotal;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    @Schema(description = "订单总金额", example = "6999.00")
    private BigDecimal totalAmount;

    @Column(name = "deposit_amount", precision = 10, scale = 2)
    @Schema(description = "定金金额", example = "2000.00")
    private BigDecimal depositAmount;

    @Column(name = "paid_amount", precision = 10, scale = 2)
    @Schema(description = "已付金额", example = "6999.00")
    private BigDecimal paidAmount;

    @Column(name = "status", nullable = false, length = 50)
    @Schema(description = "订单状态: 待付定金/已付定金/拍摄中/选片中/修片中/产品制作中/已完成/已取消", example = "待付定金")
    private String status;

    @Column(name = "current_stage")
    @Schema(description = "当前阶段: 0-待付定金 1-定金已付 2-拍摄中 3-选片中 4-修片中 5-产品制作中 6-交付完成", example = "0")
    private Integer currentStage;

    @Column(name = "shooting_date")
    @Schema(description = "拍摄日期")
    private LocalDateTime shootingDate;

    @Column(name = "shooting_location", length = 200)
    @Schema(description = "拍摄地点", example = "北京朝阳区xx摄影基地")
    private String shootingLocation;

    @Column(name = "photographer", length = 50)
    @Schema(description = "摄影师", example = "李摄影")
    private String photographer;

    @Column(name = "makeup_artist", length = 50)
    @Schema(description = "化妆师", example = "王化妆")
    private String makeupArtist;

    @Column(name = "retoucher", length = 50)
    @Schema(description = "修图师", example = "张修图")
    private String retoucher;

    @Column(name = "channel_source", length = 50)
    @Schema(description = "订单来源: 美团/抖音/小程序/门店/其他", example = "美团")
    private String channelSource;

    @Column(name = "channel_order_no", length = 100)
    @Schema(description = "第三方平台订单号")
    private String channelOrderNo;

    @Column(name = "contract_signed")
    @Schema(description = "合同是否签署: 0-未签 1-已签", example = "0")
    private Integer contractSigned;

    @Column(name = "contract_signed_time")
    @Schema(description = "合同签署时间")
    private LocalDateTime contractSignedTime;

    @Column(name = "contract_sign_ip", length = 50)
    @Schema(description = "合同签署IP")
    private String contractSignIp;

    @Column(name = "album_id")
    @Schema(description = "云相册ID")
    private Long albumId;

    @Column(name = "remark", length = 1000)
    @Schema(description = "订单备注")
    private String remark;

    @Column(name = "stage_1_time")
    @Schema(description = "阶段1完成时间(定金)")
    private LocalDateTime stage1Time;

    @Column(name = "stage_2_time")
    @Schema(description = "阶段2完成时间(拍摄)")
    private LocalDateTime stage2Time;

    @Column(name = "stage_3_time")
    @Schema(description = "阶段3完成时间(选片)")
    private LocalDateTime stage3Time;

    @Column(name = "stage_4_time")
    @Schema(description = "阶段4完成时间(修图)")
    private LocalDateTime stage4Time;

    @Column(name = "stage_5_time")
    @Schema(description = "阶段5完成时间(产品制作)")
    private LocalDateTime stage5Time;

    @Column(name = "stage_6_time")
    @Schema(description = "阶段6完成时间(交付)")
    private LocalDateTime stage6Time;

    @Column(name = "create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (currentStage == null) {
            currentStage = 0;
        }
        if (status == null) {
            status = "待付定金";
        }
        if (contractSigned == null) {
            contractSigned = 0;
        }
        if (addOnTotal == null) {
            addOnTotal = BigDecimal.ZERO;
        }
        if (paidAmount == null) {
            paidAmount = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
