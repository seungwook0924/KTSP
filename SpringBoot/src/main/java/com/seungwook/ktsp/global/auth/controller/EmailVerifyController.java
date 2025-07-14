package com.seungwook.ktsp.global.auth.controller;

import com.seungwook.ktsp.global.auth.dto.request.VerifyRequest;
import com.seungwook.ktsp.global.auth.service.AuthService;
import com.seungwook.ktsp.global.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class EmailVerifyController {

    private final AuthService authService;

    // 인증코드 발송
    @PostMapping("/{email}/send")
    public ResponseEntity<Response<Void>> mailSend(@PathVariable String email) {
        String userEmail = email + "@kangwon.ac.kr";

        authService.sendAuthCode(userEmail);

        return ResponseEntity.ok(Response.<Void>builder()
                .message("인증코드 메일 발송 성공")
                .build());
    }

    // 인증코드 검증
    @PostMapping("/verify")
    public ResponseEntity<Response<Void>> verify(@Valid @RequestBody VerifyRequest request) {
        String userEmail = request.getEmail() + "@kangwon.ac.kr";

        authService.verifyAuthCode(userEmail, request.getVerifyCode().toUpperCase());

        return ResponseEntity.ok(Response.<Void>builder()
                .message("인증 성공")
                .build());
    }
}
