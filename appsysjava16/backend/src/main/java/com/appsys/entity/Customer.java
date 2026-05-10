package com.appsys.entity;

import com.appsys.util.AesEncryptionUtil;
import com.appsys.validation.ChinesePhone;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 客户实体�? * 管理客户信息，敏感字段（手机号、地址）采用AES-256加密存储
 * 支持临时客户注册，便于展会或下乡推广时快速下�? * 
 * @author appsys-team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
@Schema(description = "客户信息")
public class Customer {

    /**
     * 客户ID，自增主�?     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "客户ID", example = "1")
    private Long id;

    /**
     * 客户名称
     */
    @NotBlank(message = "客户名称不能为空")
    @Column(nullable = false, length = 100)
    @Schema(description = "客户名称", example = "张三")
    private String name;

    /**
     * 手机号（加密存储）：符合中国大陆手机号格�?     */
    @ChinesePhone
    @Column(nullable = false, length = 255)
    @Schema(description = "客户手机号（加密存储�?, example = "13800138000")
    private String phone;

    /**
     * 客户等级：根据累计消费金额自动计�?     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "客户等级", example = "VIP")
    private CustomerLevel level = CustomerLevel.TEMPORARY;

    /**
     * 信用额度
     */
    @Column(precision = 10, scale = 2)
    @Schema(description = "信用额度", example = "10000.00")
    private BigDecimal creditLimit = BigDecimal.ZERO;

    /**
     * 已使用信用额�?     */
    @Column(precision = 10, scale = 2)
    @Schema(description = "已使用信用额�?, example = "5000.00")
    private BigDecimal usedCredit = BigDecimal.ZERO;

    /**
     * 累计消费金额
     */
    @Column(precision = 12, scale = 2)
    @Schema(description = "累计消费金额", example = "25000.00")
    private BigDecimal totalPurchaseAmount = BigDecimal.ZERO;

    /**
     * 地址（加密存储）
     */
    @Column(length = 500)
    @Schema(description = "客户地址（加密存储）", example = "北京市朝阳区XX街道XX�?)
    private String address;

    /**
     * 备注信息
     */
    @Column(columnDefinition = "TEXT")
    @Schema(description = "备注信息", example = "老客户，需要优先配�?)
    private String remarks;

    /**
     * 是否临时客户：展会或下乡推广时快速注册的客户
     */
    @Column(nullable = false)
    @Schema(description = "是否临时客户", example = "false")
    private Boolean isTemporary = false;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    @Schema(description = "创建时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;

    /**
     * 获取解密后的手机�?     * @return 原始手机�?     */
    @Transient
    @Schema(hidden = true)
    public String getDecryptedPhone() {
        return AesEncryptionUtil.Encryptor.decrypt(this.phone);
    }

    /**
     * 获取解密后的地址
     * @return 原始地址
     */
    @Transient
    @Schema(hidden = true)
    public String getDecryptedAddress() {
        if (this.address == null) return null;
        return AesEncryptionUtil.Encryptor.decrypt(this.address);
    }

    /**
     * 设置加密后的手机�?     * @param rawPhone 原始手机�?     */
    public void setEncryptedPhone(String rawPhone) {
        this.phone = AesEncryptionUtil.Encryptor.encrypt(rawPhone);
    }

    /**
     * 设置加密后的地址
     * @param rawAddress 原始地址
     */
    public void setEncryptedAddress(String rawAddress) {
        if (rawAddress != null) {
            this.address = AesEncryptionUtil.Encryptor.encrypt(rawAddress);
        }
    }

    /**
     * 获取可用信用额度
     * @return 可用信用额度
     */
    @Transient
    @Schema(hidden = true)
    public BigDecimal getAvailableCredit() {
        return creditLimit.subtract(usedCredit);
    }

    /**
     * 兼容前端getNotes
     * @return 备注信息
     */
    @Transient
    @Schema(hidden = true)
    public String getNotes() {
        return this.remarks;
    }

    /**
     * 兼容前端setNotes
     * @param notes 备注信息
     */
    public void setNotes(String notes) {
        this.remarks = notes;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
