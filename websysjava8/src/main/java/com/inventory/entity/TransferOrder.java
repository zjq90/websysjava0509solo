package com.inventory.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 调拨单实体类
 * 用于管理库存调拨操作
 * 支持仓库之间、仓库到门店的调拨
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "transfer_order")
public class TransferOrder extends BaseEntity {

    /**
     * 调拨单号，唯一标识
     */
    @Column(name = "order_no", nullable = false, unique = true, length = 50)
    private String orderNo;

    /**
     * 调拨类型：
     * 1-仓库到仓库
     * 2-仓库到门店
     */
    @Column(name = "transfer_type", nullable = false)
    private Integer transferType;

    /**
     * 调出位置ID
     */
    @Column(name = "from_location_id", nullable = false)
    private Long fromLocationId;

    /**
     * 调出位置类型：1-仓库，2-门店
     */
    @Column(name = "from_location_type", nullable = false)
    private Integer fromLocationType;

    /**
     * 调入位置ID
     */
    @Column(name = "to_location_id", nullable = false)
    private Long toLocationId;

    /**
     * 调入位置类型：1-仓库，2-门店
     */
    @Column(name = "to_location_type", nullable = false)
    private Integer toLocationType;

    /**
     * 调拨总数量
     */
    @Column(name = "total_quantity", nullable = false)
    private Integer totalQuantity;

    /**
     * 状态：
     * 0-待审核
     * 1-已审核
     * 2-出库中
     * 3-已完成
     * 4-已取消
     */
    @Column(name = "status", nullable = false)
    private Integer status = 0;

    /**
     * 申请人
     */
    @Column(name = "applicant", length = 50)
    private String applicant;

    /**
     * 申请时间
     */
    @Column(name = "apply_time")
    private LocalDateTime applyTime;

    /**
     * 审核人
     */
    @Column(name = "auditor", length = 50)
    private String auditor;

    /**
     * 审核时间
     */
    @Column(name = "audit_time")
    private LocalDateTime auditTime;

    /**
     * 出库人
     */
    @Column(name = "outbound_person", length = 50)
    private String outboundPerson;

    /**
     * 出库时间
     */
    @Column(name = "outbound_time")
    private LocalDateTime outboundTime;

    /**
     * 入库人
     */
    @Column(name = "inbound_person", length = 50)
    private String inboundPerson;

    /**
     * 入库时间
     */
    @Column(name = "inbound_time")
    private LocalDateTime inboundTime;

    /**
     * 调拨原因
     */
    @Column(name = "reason", length = 200)
    private String reason;

    /**
     * 备注
     */
    @Column(name = "remark", length = 500)
    private String remark;
}
