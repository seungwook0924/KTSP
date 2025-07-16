package com.seungwook.ktsp.domain.user.dto.response;

import lombok.Getter;

@Getter
public class UserProfileResponse {

    private final String name;
    private final String major;
    private final String studentNumber;

    public UserProfileResponse(String name, String major, String studentNumber) {
        this.name = name;
        this.major = major;
        this.studentNumber = studentNumber;
    }
}
