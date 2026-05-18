package com.heritage.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 鉴定申请实体类
 * 
 * @author Heritage Team
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "authentication_request")
public class AuthenticationRequest extends BaseEntity {

    /**
     * 申请单号
     */
    @Column(name = "request_no", unique = true, nullable = false, length = 50)
    private String requestNo;

    /**
     * 申请人ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 文物名称
     */
    @Column(name = "heritage_name", nullable = false, length = 200)
    private String heritageName;

    /**
     * 文物类别
     */
    @Column(name = "category", nullable = false)
    private Integer category;

    /**
     * 文物描述
     */
    @Column(name = "description", length = 2000)
    private String description;

    /**
     * 多角度照片URL（多个用逗号分隔，至少5张）
     */
    @Column(name = "photos", length = 3000)
    private String photos;

    /**
     * 描述视频URL（1分钟）
     */
    @Column(name = "video_url", length = 500)
    private String videoUrl;

    /**
     * 鉴定过程录像URL
     */
    @Column(name = "process_video_url", length = 500)
    private String processVideoUrl;

    /**
     * 匹配的专家ID
     */
    @Column(name = "expert_id")
    private Long expertId;

    /**
     * 状态：0-待审核，1-已匹配专家，2-鉴定中，3-已完成，4-已拒绝，5-已取消
     */
    @Column(name = "status", nullable = false)
    private Integer status = 0;

    /**
     * 鉴定结果：1-真品，2-赝品，3-存疑
     */
    @Column(name = "result")
    private Integer result;

    /**
     * 鉴定意见
     */
    @Column(name = "expert_opinion", length = 2000)
    private String expertOpinion;

    /**
     * 估值
     */
    @Column(name = "estimated_value", precision = 15, scale = 2)
    private BigDecimal estimatedValue;

    /**
     * 鉴定报告URL（电子版）
     */
    @Column(name = "report_url", length = 500)
    private String reportUrl;

    /**
     * 是否需要纸质报告：0-不需要，1-需要
     */
    @Column(name = "need_paper_report", nullable = false)
    private Integer needPaperReport = 0;

    /**
     * 纸质报告邮寄地址（RSA加密）
     */
    @Column(name = "shipping_address", length = 500)
    private String shippingAddress;

    /**
     * 备注
     */
    @Column(name = "remark", length = 1000)
    private String remark;
}