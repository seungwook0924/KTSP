package com.seungwook.ktsp.domain.user.controller;

import com.seungwook.ktsp.domain.user.dto.response.UserInfoResponse;
import com.seungwook.ktsp.domain.user.dto.UserProfile;
import com.seungwook.ktsp.domain.user.dto.response.UserProfileResponse;
import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.domain.user.entity.enums.UserStatus;
import com.seungwook.ktsp.domain.user.mapper.UserResponseMapper;
import com.seungwook.ktsp.domain.user.service.UserQueryService;
import com.seungwook.ktsp.global.auth.service.AuthService;
import com.seungwook.ktsp.global.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Query", description = "회원 정보 조회 API (내 정보 및 타인 프로필)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/service/user")
public class UserQueryController {

    private final AuthService authService;
    private final UserQueryService userQueryService;

    // 내 정보 조회
    @Operation(summary = "회원 정보 조회", description = "이메일, 이름, 학년, 학번, 전화번호, 전공, 직전학기 성적, 캠퍼스, 자기소개 조회")
    @GetMapping
    public ResponseEntity<Response<UserInfoResponse>> getMyInfo() {

        User user = userQueryService.getUserInformation(authService.getUserId());
        UserInfoResponse response = UserResponseMapper.toUserInfoResponse(user);

        return ResponseEntity.ok(Response.<UserInfoResponse>builder()
                .message("내 정보를 불러왔습니다.")
                .data(response)
                .build());
    }

    // 특정 회원 프로필 조회
    @Operation(summary = "특정 회원 프로필 조회", description = "이름, 학번, 전공, 소개 리턴, 탈퇴한 회원일 경우 null")
    @GetMapping("/{userId}")
    public ResponseEntity<Response<UserProfileResponse>> getUserProfile(
            @Parameter(description = "UserId(PK)", example = "1")
            @PathVariable Long userId) {

        UserProfile userProfile = userQueryService.getUserProfile(userId);

        // 탈퇴한 회원이면 data 없음
        if(userProfile.getUserStatus().equals(UserStatus.WITHDRAWN)) {
            return ResponseEntity.ok(Response.<UserProfileResponse>builder()
                    .message("탈퇴한 회원입니다.")
                    .build());
        }

        UserProfileResponse response = UserResponseMapper.toUserProfileResponse(userProfile);
        return ResponseEntity.ok(Response.<UserProfileResponse>builder()
                .message("회원 프로필 조회에 성공했습니다.")
                .data(response)
                .build());
    }
}
