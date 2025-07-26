package com.seungwook.ktsp.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProfileResponse {

    private final String name;
    private final String major;
    private final String studentNumber;
    private final String introduction;
}
