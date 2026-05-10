package com.appsys.order.entity;

import com.appsys.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 客户实体类
 * 客户联系方式使用AES加密存储
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "customer", indexes = {
    @Index(name = "idx_deleted", columnList = "deleted")
})
@Schema(description = "客户信息")
public class Customer extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "客户名称")
    @Column(name = "customer_name", nullable = false, length = 100)
    private String customerName;

    @Schema(description = "手机号（加密存储）")
    @Column(name = "phone", length = 255)
    private String phone;

    @Schema(description = "地址")
    @Column(name = "address", length = 255)
    private String address;

    @Schema(description = "邮箱")
    @Column(name = "email", length = 100)
    private String email;

    @Schema(description = "客户类型")
    @Column(name = "customer_type", length = 50)
    private String customerType;

    @Schema(description = "备注")
    @Column(name = "remark", length = 500)
    private String remark;
}
