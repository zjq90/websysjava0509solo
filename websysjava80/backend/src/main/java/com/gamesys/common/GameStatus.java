package com.gamesys.common;

import lombok.Getter;

@Getter
public enum GameStatus {
    PENDING(0, "待审核"),
    ONLINE(1, "已上线"),
    OFFLINE(2, "已下架"),
    REJECTED(3, "审核拒绝");

    private final Integer code;
    private final String desc;

    GameStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
