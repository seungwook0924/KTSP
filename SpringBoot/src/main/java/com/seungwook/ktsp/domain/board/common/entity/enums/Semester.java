package com.seungwook.ktsp.domain.board.common.entity.enums;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public enum Semester {
    FIRST("1학기"),
    SECOND("2학기");

    private final String label;


    Semester(String label) {
        this.label = label;
    }

    /*
        1월 ~ 6월: 1학기
        7월 ~ 12월: 2학기
        러닝코어, 메이저러너, 챌린지러너는 주로 학기 시작 이전 ~ 학기 초에 신청하기 때문
    */
    public static Semester resolveByDate() {
        return (LocalDate.now().getMonthValue() <= 6) ? FIRST : SECOND;
    }
}
