package com.seungwook.ktsp.domain.board.common.entity.enums;

import lombok.Getter;

@Getter
public enum TeamJoinStatus {
    PENDING("승인 대기"),
    REJECTED("승인 거절"),
    EXPELLED("추방"),
    APPROVED("소속");

    private final String label;

    TeamJoinStatus(String label) {
        this.label = label;
    }
}
