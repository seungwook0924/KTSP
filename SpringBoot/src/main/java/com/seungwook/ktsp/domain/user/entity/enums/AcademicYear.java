package com.seungwook.ktsp.domain.user.entity.enums;

import lombok.Getter;

@Getter
public enum AcademicYear {
    FIRST_YEAR("1학년"),
    SECOND_YEAR("2학년"),
    THIRD_YEAR("3학년"),
    FOURTH_YEAR("4학년"),
    GRADUATE("졸업생");

    private final String label;

    AcademicYear(String label) {
        this.label = label;
    }
}
