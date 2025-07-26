package com.seungwook.ktsp.domain.user.mapper;

import com.seungwook.ktsp.domain.board.common.dto.response.Writer;
import com.seungwook.ktsp.domain.user.dto.WriterInfo;
import com.seungwook.ktsp.domain.user.dto.response.UserInfoResponse;
import com.seungwook.ktsp.domain.user.dto.UserProfile;
import com.seungwook.ktsp.domain.user.dto.response.UserProfileResponse;
import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.domain.user.entity.enums.UserStatus;

public class UserMapper {

    // UserInfoResponse로 변환
    public static UserInfoResponse toUserInfoResponse(User user) {
        return new UserInfoResponse(
                user.getEmail(),
                user.getName(),
                user.getAcademicYear().getLabel(),
                user.getStudentNumber(),
                user.getPhoneNumber(),
                user.getMajor(),
                user.getPreviousGpa(),
                user.getCampus().getLabel(),
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

    // WriterInfo -> Writer로 변환
    public static Writer toWriter(WriterInfo writerInfo) {
        if (writerInfo.getUserStatus().equals(UserStatus.WITHDRAWN))
            return new Writer("탈퇴한 사용자", "알 수 없음", "알 수 없음");

        return new Writer(writerInfo.getName(),
                writerInfo.getMajor(),
                writerInfo.getStudentNumber().substring(2, 4) + "학번"
        );
    }
}
