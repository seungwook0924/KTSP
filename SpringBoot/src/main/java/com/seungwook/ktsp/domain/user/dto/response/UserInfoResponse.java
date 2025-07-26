package com.seungwook.ktsp.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class UserInfoResponse {

    private final String email;
    private final String name;
    private final String academicYear;
    private final String studentNumber;
    private final String phoneNumber;
    private final String major;
    private final BigDecimal previousGpa;
    private final String campus;
    private final String introduction;
}
