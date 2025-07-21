package com.seungwook.ktsp.domain.user.controller;

import com.seungwook.ktsp.domain.user.dto.request.PasswordUpdateRequest;
import com.seungwook.ktsp.domain.user.dto.request.UserInfoUpdateRequest;
import com.seungwook.ktsp.domain.user.dto.request.WithdrawnRequest;
import com.seungwook.ktsp.domain.user.dto.response.UserInfoResponse;
import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.domain.user.mapper.UserResponseMapper;
import com.seungwook.ktsp.domain.user.service.UserCommandService;
import com.seungwook.ktsp.global.auth.dto.UserSession;
import com.seungwook.ktsp.global.auth.service.AuthService;
import com.seungwook.ktsp.global.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User")
@RestController
@RequiredArgsConstructor
@RequestMapping("/service/user")
public class UserCommandController {

    private final AuthService authService;
    private final UserCommandService userCommandService;

    // 내 정보 수정
    @Operation(summary = "회원 정보 수정", description = "학년, 전화번호, 전공, 직전학기 성적, 캠퍼스, 자기소개 등 수정")
    @PatchMapping
    public ResponseEntity<Response<UserInfoResponse>> updateMyInfo(@Valid @RequestBody UserInfoUpdateRequest request) {

        User user = userCommandService.updateUserInformation(authService.getUserId(), request);
        UserInfoResponse response = UserResponseMapper.toUserInfoResponse(user);

        return ResponseEntity.ok(Response.<UserInfoResponse>builder()
                .message("내 정보를 수정했습니다.")
                .data(response)
                .build());
    }

    // 비밀번호 변경
    @Operation(summary = "회원 비밀번호 변경", description = "기존 비밀번호와 신규 비밀번호를 제출하면 신규 비밀번호로 변경")
    @PatchMapping("/password")
    public ResponseEntity<Response<Void>> updatePassword(@Valid @RequestBody PasswordUpdateRequest request) {

        userCommandService.updatePassword(authService.getUserId(), request);

        return ResponseEntity.ok(Response.<Void>builder()
                .message("비밀번호가 변경되었습니다.")
                .build());
    }

    // 회원 탈퇴
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴, 자동 로그인 상태에선 요청 거부")
    @DeleteMapping
    public ResponseEntity<Response<Void>> withdrawnUser(@AuthenticationPrincipal UserSession userSession,
                                                        @Valid @RequestBody WithdrawnRequest request,
                                                        HttpServletRequest httpRequest,
                                                        HttpServletResponse httpResponse) {

        userCommandService.withdrawnUser(userSession, request, httpRequest, httpResponse);

        return ResponseEntity.ok(Response.<Void>builder()
                .message("탈퇴가 완료되었습니다.")
                .build());
    }
}
