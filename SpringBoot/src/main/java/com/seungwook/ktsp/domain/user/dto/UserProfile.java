package com.seungwook.ktsp.domain.user.dto;

import com.seungwook.ktsp.domain.user.entity.enums.UserStatus;
import lombok.Getter;

@Getter
public class UserProfile {

    private final String name;
    private final String major;
    private final String studentNumber;
    private final String introduction;
    private final UserStatus userStatus;

    public UserProfile(String name, String major, String studentNumber, String introduction, UserStatus userStatus) {
        this.name = name;
        this.major = major;
        this.studentNumber = studentNumber;
        this.introduction = introduction;
        this.userStatus = userStatus;
    }
}
