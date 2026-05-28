package com.club.entity.enums;

import lombok.Getter;

/**
 * 社团成员角色枚举
 *
 * @author club-management
 * @version 1.0.0
 */
@Getter
public enum MemberRoleEnum {

    NORMAL("普通成员", 1),
    DEPARTMENT_HEAD("部门负责人", 2),
    VICE_PRESIDENT("副社长", 3),
    PRESIDENT("社长", 4);

    private final String desc;
    private final Integer level;

    MemberRoleEnum(String desc, Integer level) {
        this.desc = desc;
        this.level = level;
    }
}
