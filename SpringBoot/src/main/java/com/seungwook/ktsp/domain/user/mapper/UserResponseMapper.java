package com.seungwook.ktsp.domain.user.mapper;

import com.seungwook.ktsp.domain.user.dto.response.UserInfoResponse;
import com.seungwook.ktsp.domain.user.entity.User;

public class UserResponseMapper {

    // MyInfoResponse로 변환
    public static UserInfoResponse toUserInfoResponse(User user) {
        return new UserInfoResponse(user.getEmail(),
                user.getName(),
                user.getAcademicYear(),
                user.getStudentNumber(),
                user.getPhoneNumber(),
                user.getPhoneNumber(),
                user.getPreviousGpa(),
                user.getCampus(),
                user.getIntroduction());
    }
}
