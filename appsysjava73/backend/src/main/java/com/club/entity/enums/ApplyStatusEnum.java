package com.club.entity.enums;

import lombok.Getter;

/**
 * 招新申请状态枚举
 *
 * @author club-management
 * @version 1.0.0
 */
@Getter
public enum ApplyStatusEnum {

    PENDING("待审核", "#FFA500"),
    APPROVED("已通过", "#50C878"),
    REJECTED("已驳回", "#E24A68");

    private final String desc;
    private final String color;

    ApplyStatusEnum(String desc, String color) {
        this.desc = desc;
        this.color = color;
    }
}
