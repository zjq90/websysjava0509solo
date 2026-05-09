package com.inventory.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;

/**
 * 种子品类实体类
 * 用于管理种子的大类信息，例如：谷物类、蔬菜类、水果类等
 * 一个品类下可以包含多个品种
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "category")
public class Category extends BaseEntity {

    /**
     * 品类名称，唯一且不为空
     */
    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    /**
     * 品类编码，用于快速识别
     */
    @Column(name = "code", unique = true, length = 50)
    private String code;

    /**
     * 品类描述，详细说明该品类的特点
     */
    @Column(name = "description", length = 500)
    private String description;

    /**
     * 状态：1-启用，0-禁用
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;
}
