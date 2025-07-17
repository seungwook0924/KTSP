package com.seungwook.ktsp.domain.user.dto.response;

import com.seungwook.ktsp.domain.user.entity.enums.AcademicYear;
import com.seungwook.ktsp.domain.user.entity.enums.Campus;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class UserInfoResponse {

    private final String email;
    private final String name;
    private final AcademicYear academicYear;
    private final String studentNumber;
    private final String phoneNumber;
    private final String major;
    private final BigDecimal previousGpa;
    private final Campus campus;
    private final String introduction;

    public UserInfoResponse(String email, String name, AcademicYear academicYear, String studentNumber, String phoneNumber, String major, BigDecimal previousGpa, Campus campus, String introduction) {
        this.email = email;
        this.name = name;
        this.academicYear = academicYear;
        this.studentNumber = studentNumber;
        this.phoneNumber = phoneNumber;
        this.major = major;
        this.previousGpa = previousGpa;
        this.campus = campus;
        this.introduction = introduction;
    }
}
