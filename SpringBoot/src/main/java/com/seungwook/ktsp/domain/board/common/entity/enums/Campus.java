package com.seungwook.ktsp.domain.board.common.entity.enums;

import lombok.Getter;

@Getter
public enum Campus {
    CHUNCHEON("춘천"),
    SAMCHEOK("삼척"),
    DOGYE("도계"),
    NONE("상관 없음");

    private final String label;

    Campus(String label) {
        this.label = label;
    }
}

