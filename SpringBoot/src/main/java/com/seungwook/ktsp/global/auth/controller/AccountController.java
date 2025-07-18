package com.seungwook.ktsp.global.auth.controller;

import com.seungwook.ktsp.global.auth.dto.request.LoginRequest;
import com.seungwook.ktsp.global.auth.dto.request.PasswordResetRequest;
import com.seungwook.ktsp.global.auth.dto.request.RegisterRequest;
import com.seungwook.ktsp.global.auth.dto.response.LoginResponse;
import com.seungwook.ktsp.global.auth.service.AccountService;
import com.seungwook.ktsp.global.auth.service.AuthService;
import com.seungwook.ktsp.global.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Account", description = "회원가입, 로그인, 로그아웃 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final AuthService authService;

    // 회원가입
    @Operation(summary = "회원가입", description = "이메일 인증을 완료한 사용자 회원가입")
    @PostMapping("/register")
    public ResponseEntity<Response<Void>> register(@Valid @RequestBody RegisterRequest request, HttpServletRequest httpRequest) {

        accountService.register(request, httpRequest);

        return ResponseEntity.ok(Response.<Void>builder()
                .message("회원가입 성공")
                .build());
    }

    // 로그인
    @Operation(summary = "로그인", description = "학번과 비밀번호를 통해 로그인, 세션 기반 인증 수행")
    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {

        LoginResponse response = authService.login(request, httpRequest);

        return ResponseEntity.ok(Response.<LoginResponse>builder()
                .message("로그인 성공")
                .data(response)
                .build());
    }

    // 비밀번호 찾기
    @Operation(summary = "비밀번호 찾기", description = "이메일 인증을 완료한 사용자 비밀번호 변경 가능")
    @PostMapping("/find-password")
    public ResponseEntity<Response<Void>> findPassword(@Valid @RequestBody PasswordResetRequest request, HttpServletRequest httpServletRequest) {

        accountService.resetPassword(request, httpServletRequest);

        return ResponseEntity.ok(Response.<Void>builder()
                .message("비밀번호 변경 성공")
                .build());
    }

    // 로그아웃
    @Operation(summary = "로그아웃", description = "현재 사용자 세션을 무효화하여 로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {

        authService.logout(request);

        return ResponseEntity.noContent()
                .build(); // 204 No Content
    }
}
