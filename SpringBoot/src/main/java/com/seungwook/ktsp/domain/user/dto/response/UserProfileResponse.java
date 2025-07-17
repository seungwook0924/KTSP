package com.seungwook.ktsp.domain.user.dto.response;

import lombok.Getter;

@Getter
public class UserProfileResponse {

    private final String name;
    private final String major;
    private final String studentNumber;
    private final String introduction;

    public UserProfileResponse(String name, String major, String studentNumber, String introduction) {
        this.name = name;
        this.major = major;
        this.studentNumber = studentNumber;
        this.introduction = introduction;
    }
}
