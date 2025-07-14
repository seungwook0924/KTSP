package com.seungwook.ktsp.global.auth.controller;

import com.seungwook.ktsp.global.auth.dto.request.LoginRequest;
import com.seungwook.ktsp.global.auth.dto.request.RegisterRequest;
import com.seungwook.ktsp.global.auth.service.AuthService;
import com.seungwook.ktsp.global.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<Response<Void>> login(@Valid @RequestBody RegisterRequest request) {

        authService.register(request);

        return ResponseEntity.ok(Response.<Void>builder()
                .message("회원가입 성공")
                .build());
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Response<Void>> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {

        authService.login(request, httpRequest);

        return ResponseEntity.ok(Response.<Void>builder()
                .message("로그인 성공")
                .build());
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {

        authService.logout(request);

        return ResponseEntity.noContent()
                .build(); // 204 No Content
    }
}
