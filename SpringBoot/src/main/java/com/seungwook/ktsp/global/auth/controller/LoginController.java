package com.seungwook.ktsp.global.auth.controller;

import com.seungwook.ktsp.global.auth.dto.request.LoginRequest;
import com.seungwook.ktsp.global.auth.service.AuthService;
import com.seungwook.ktsp.global.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final AuthService authService;

    @PostMapping("/public/login")
    public ResponseEntity<Response<Void>> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {

        authService.login(request, httpRequest);

        return ResponseEntity.ok(Response.<Void>builder()
                .success(true)
                .message("로그인 성공")
                .build());
    }
}
