package com.seungwook.ktsp.domain.user.dto.response;

import com.seungwook.ktsp.domain.user.entity.enums.AcademicYear;
import com.seungwook.ktsp.domain.user.entity.enums.Campus;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class MyInfoResponse {

    private final String email;
    private final String name;
    private final AcademicYear academicYear;
    private final String studentNumber;
    private final String phoneNumber;
    private final String major;
    private final BigDecimal previousGpa;
    private final Campus campus;

    public MyInfoResponse(String email, String name, AcademicYear academicYear, String studentNumber, String phoneNumber, String major, BigDecimal previousGpa, Campus campus) {
        this.email = email;
        this.name = name;
        this.academicYear = academicYear;
        this.studentNumber = studentNumber;
        this.phoneNumber = phoneNumber;
        this.major = major;
        this.previousGpa = previousGpa;
        this.campus = campus;
    }
}
