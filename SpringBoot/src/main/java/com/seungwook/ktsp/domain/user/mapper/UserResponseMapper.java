package com.seungwook.ktsp.domain.user.mapper;

import com.seungwook.ktsp.domain.user.dto.response.UserInfoResponse;
import com.seungwook.ktsp.domain.user.dto.UserProfile;
import com.seungwook.ktsp.domain.user.dto.response.UserProfileResponse;
import com.seungwook.ktsp.domain.user.entity.User;

public class UserResponseMapper {

    // UserInfoResponse로 변환
    public static UserInfoResponse toUserInfoResponse(User user) {
        return new UserInfoResponse(
                user.getEmail(),
                user.getName(),
                user.getAcademicYear(),
                user.getStudentNumber(),
                user.getPhoneNumber(),
                user.getMajor(),
                user.getPreviousGpa(),
                user.getCampus(),
                user.getIntroduction()
        );
    }

    // UserProfileResponse로 변환
    public static UserProfileResponse toUserProfileResponse(UserProfile userProfile) {
        return new UserProfileResponse(
                userProfile.getName(),
                userProfile.getMajor(),
                userProfile.getStudentNumber().substring(2, 4) + "학번",
                userProfile.getIntroduction()
        );
    }
}
