package com.seungwook.ktsp.domain.user.dto;

import com.seungwook.ktsp.domain.user.entity.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WriterInfo {
    private final long userId;
    private final String name;
    private final String major;
    private final String studentNumber;
    private final UserStatus userStatus;
}
