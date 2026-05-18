package com.flowerstore.backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 企业定制实体类
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "enterprise_custom")
@Schema(description = "企业定制")
public class EnterpriseCustom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "定制ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Column(length = 200)
    @Schema(description = "企业名称")
    private String enterpriseName;

    @Column(length = 50)
    @Schema(description = "联系人")
    private String contactPerson;

    @Column(length = 20)
    @Schema(description = "联系电话")
    private String contactPhone;

    @Column(length = 100)
    @Schema(description = "联系邮箱")
    private String contactEmail;

    @Column(length = 500)
    @Schema(description = "企业Logo URL")
    private String enterpriseLogo;

    @Column(length = 500)
    @Schema(description = "定制祝福语")
    private String customMessage;

    @Column(length = 1000)
    @Schema(description = "定制需求描述")
    private String requirement;

    @Column(length = 500)
    @Schema(description = "Excel文件URL（批量订单）")
    private String excelFile;

    @Schema(description = "预计数量")
    private Integer estimatedQuantity;

    @Schema(description = "预估总价（分）")
    private Long estimatedTotalPrice;

    @Schema(description = "状态：0-待审核，1-已确认，2-已完成，3-已取消")
    private Integer status;

    @Column(length = 500)
    @Schema(description = "审核备注")
    private String auditRemark;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "审核时间")
    private LocalDateTime auditTime;

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
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
