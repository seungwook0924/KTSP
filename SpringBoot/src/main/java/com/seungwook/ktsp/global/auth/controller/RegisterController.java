package com.seungwook.ktsp.global.auth.controller;

import com.seungwook.ktsp.global.auth.dto.request.LoginRequest;
import com.seungwook.ktsp.global.auth.dto.request.RegisterRequest;
import com.seungwook.ktsp.global.auth.service.AuthService;
import com.seungwook.ktsp.global.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegisterController {

    private final AuthService authService;

    @PostMapping("/auth/register")
    public ResponseEntity<Response<Void>> login(@Valid @RequestBody RegisterRequest request) {

        authService.register(request);

        return ResponseEntity.ok(Response.<Void>builder()
                .success(true)
                .message("회원가입 성공")
                .build());
    }
}
