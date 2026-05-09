package com.inventory.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;

/**
 * 门店实体类
 * 用于管理门店信息，支持多门店管理
 * 门店可以从仓库调拨种子进行销售
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "store")
public class Store extends BaseEntity {

    /**
     * 门店名称
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * 门店编码，唯一标识
     */
    @Column(name = "code", unique = true, length = 50)
    private String code;

    /**
     * 门店地址
     */
    @Column(name = "address", length = 200)
    private String address;

    /**
     * 门店负责人
     */
    @Column(name = "manager", length = 50)
    private String manager;

    /**
     * 联系电话
     */
    @Column(name = "phone", length = 20)
    private String phone;

    /**
     * 门店类型：
     * 1-直营店
     * 2-加盟店
     * 3-授权经销商
     */
    @Column(name = "type", nullable = false)
    private Integer type = 1;

    /**
     * 所属区域
     */
    @Column(name = "region", length = 100)
    private String region;

    /**
     * 状态：1-营业中，0-已关闭
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    /**
     * 备注
     */
    @Column(name = "remark", length = 500)
    private String remark;
}
