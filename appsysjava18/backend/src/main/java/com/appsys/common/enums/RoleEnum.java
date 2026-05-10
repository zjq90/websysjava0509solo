package com.appsys.common.enums;

/**
 * 角色枚举类
 * 定义系统中的角色类型
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
public enum RoleEnum {

    WAREHOUSE_KEEPER("ROLE_WAREHOUSE_KEEPER", "仓管员", "库存操作"),
    SALESMAN("ROLE_SALESMAN", "销售员", "订单创建，报表查看"),
    AGRICULTURAL_TECHNICIAN("ROLE_AGRICULTURAL_TECHNICIAN", "农技员", "田间记录，报表查看"),
    MANAGER("ROLE_MANAGER", "管理层", "库存操作，订单创建，田间记录，报表查看"),
    ADMIN("ROLE_ADMIN", "系统管理员", "系统管理");

    private final String code;
    private final String name;
    private final String description;

    RoleEnum(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据code获取角色枚举
     */
    public static RoleEnum getByCode(String code) {
        for (RoleEnum role : values()) {
            if (role.getCode().equals(code)) {
                return role;
            }
        }
        return null;
    }
}
