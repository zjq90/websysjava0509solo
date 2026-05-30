package com.gamesys.common;

import lombok.Getter;

@Getter
public enum RecommendationType {
    HOME("home", "首页推荐"),
    CATEGORY("category", "分类推荐"),
    POPUP("popup", "弹窗推荐");

    private final String code;
    private final String desc;

    RecommendationType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
