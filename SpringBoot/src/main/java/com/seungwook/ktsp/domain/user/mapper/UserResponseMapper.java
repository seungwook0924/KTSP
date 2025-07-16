package com.seungwook.ktsp.domain.user.mapper;

import com.seungwook.ktsp.domain.user.dto.response.MyInfoResponse;
import com.seungwook.ktsp.domain.user.entity.User;

public class UserResponseMapper {

    // MyInfoResponse로 변환
    public static MyInfoResponse toMyinfoResponse(User user) {
        return new MyInfoResponse(user.getEmail(),
                user.getName(),
                user.getAcademicYear(),
                user.getStudentNumber(),
                user.getPhoneNumber(),
                user.getMajor(),
                user.getPreviousGpa(),
                user.getCampus());
    }
}
