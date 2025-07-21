package com.seungwook.ktsp.domain.user.controller;

import com.seungwook.ktsp.domain.user.dto.request.PasswordResetRequest;
import com.seungwook.ktsp.domain.user.dto.request.RegisterRequest;
import com.seungwook.ktsp.domain.user.service.PreAuthAccountService;
import com.seungwook.ktsp.global.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User")
@RestController
@RequiredArgsConstructor
@RequestMapping("/public/user")
public class PreAuthAccountController {

    private final PreAuthAccountService preAuthAccountService;

    // 회원가입
    @Operation(summary = "회원가입", description = "이메일 인증을 완료한 사용자 회원가입")
    @PreAuthorize("!isAuthenticated()") // 이미 인증된 상태라면 거부
    @PostMapping
    public ResponseEntity<Response<Void>> register(@Valid @RequestBody RegisterRequest request, HttpServletRequest httpRequest) {

        preAuthAccountService.register(request, httpRequest);

        return ResponseEntity.ok(Response.<Void>builder()
                .message("회원가입 성공")
                .build());
    }

    // 비밀번호 찾기
    @Operation(summary = "비밀번호 찾기", description = "이메일 인증을 완료한 사용자 비밀번호 변경 가능")
    @PreAuthorize("!isAuthenticated()") // 이미 인증된 상태라면 거부
    @PostMapping("/find-password")
    public ResponseEntity<Response<Void>> findPassword(@Valid @RequestBody PasswordResetRequest request, HttpServletRequest httpServletRequest) {

        preAuthAccountService.resetPassword(request, httpServletRequest);

        return ResponseEntity.ok(Response.<Void>builder()
                .message("비밀번호 변경 성공")
                .build());
    }
}
