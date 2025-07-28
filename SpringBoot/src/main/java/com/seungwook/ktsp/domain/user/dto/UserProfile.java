package com.seungwook.ktsp.domain.user.dto;

import com.seungwook.ktsp.domain.user.entity.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProfile {
    private final String name;
    private final String major;
    private final String studentNumber;
    private final String introduction;
    private final UserStatus userStatus;
}
