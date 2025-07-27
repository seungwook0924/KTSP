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

    public static Semester resolveByDate(LocalDate date) {
        LocalDate firstSemesterStart = LocalDate.of(date.getYear(), 1, 1); // 1학기 시작
        LocalDate firstSemesterEnd = LocalDate.of(date.getYear(), 6, 30); // 1학기 종료

        return (date.getMonthValue() <= 6) ? FIRST : SECOND;
    }
}
