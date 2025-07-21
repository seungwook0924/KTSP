package com.seungwook.ktsp.global.auth.controller;

import com.seungwook.ktsp.global.auth.dto.request.LoginRequest;
import com.seungwook.ktsp.global.auth.dto.response.LoginResponse;
import com.seungwook.ktsp.global.auth.service.AuthService;
import com.seungwook.ktsp.global.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "로그인, 로그아웃 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/public/auth")
public class AuthController {

    private final AuthService authService;

    // 로그인
    @Operation(summary = "로그인", description = "학번과 비밀번호를 통해 로그인, 세션 기반 인증 수행")
    @PreAuthorize("!isAuthenticated()") // 이미 인증된 상태라면 거부
    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {

        LoginResponse response = authService.login(request, httpRequest, httpResponse);

        return ResponseEntity.ok(Response.<LoginResponse>builder()
                .message("로그인 성공")
                .data(response)
                .build());
    }

    // 로그아웃
    @Operation(summary = "로그아웃", description = "현재 사용자 세션을 무효화하여 로그아웃")
    @PreAuthorize("isAuthenticated()") // 인증 되어있을 때만 로그아웃 가능
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {

        authService.logout(httpRequest, httpResponse);

        return ResponseEntity.noContent()
                .build(); // 204 No Content
    }
}
