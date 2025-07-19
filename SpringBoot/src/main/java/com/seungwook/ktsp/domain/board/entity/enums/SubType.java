package com.seungwook.ktsp.domain.board.entity.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

import static com.seungwook.ktsp.domain.board.entity.enums.MainType.*;

@Getter
public enum SubType {

    NONE(null, "구분 없음"),

    MENTOR(LEARNING_CORE, "배우미"),
    MENTEE(LEARNING_CORE, "가르치미"),

    MAJOR_MULTI(MAJOR_LEARNER, "전공학습(부·복수) 공동체"),
    FRESH_REJOIN(MAJOR_LEARNER, "신입·재입학생공동체"),
    TRANSFER_CHANGE(MAJOR_LEARNER, "전과·편입학생 공동체"),

    FIRST_SEMESTER(CHALLENGE_LEARNER, "1학기"),
    SECOND_SEMESTER(CHALLENGE_LEARNER, "2학기"),

    PROJECT(PROJECT_CONTEST, "프로젝트"),
    CONTEST(PROJECT_CONTEST, "공모전"),

    NOTICE(ETC, "공지사항"),
    FREE(ETC, "자유게시판"),
    REPORT(ETC, "오류·사용자 신고");

    private final MainType mainType;
    private final String label;

    SubType(MainType mainType, String label) {
        this.mainType = mainType;
        this.label = label;
    }

    public static List<SubType> getByMainType(MainType mainType) {
        return Arrays.stream(values())
                .filter(st -> st.mainType == mainType)
                .toList();
    }

    public MainType getMainType() {
        return mainType;
    }

    public String getLabel() {
        return label;
    }

    public boolean isCommon() {
        return mainType == null;
    }
}

