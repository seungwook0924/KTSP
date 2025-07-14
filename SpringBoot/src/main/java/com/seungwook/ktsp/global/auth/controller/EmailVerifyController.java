package com.seungwook.ktsp.global.auth.controller;

import com.seungwook.ktsp.global.auth.dto.request.VerifyRequest;
import com.seungwook.ktsp.global.auth.service.AuthService;
import com.seungwook.ktsp.global.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "인증/인가 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class EmailVerifyController {

    private final AuthService authService;

    // 인증코드 발송
    @Operation(summary = "이메일 인증코드 발송", description = "해당 이메일로 인증코드가 발송됨")
    @PostMapping("/{email}/send")
    public ResponseEntity<Response<Void>> mailSend(
            @Parameter(description = "이메일 앞자리(@kangwon.ac.kr가 자동으로 붙음)", example = "user123")
            @PathVariable String email) {
        String userEmail = email + "@kangwon.ac.kr";

        authService.sendAuthCode(userEmail);

        return ResponseEntity.ok(Response.<Void>builder()
                .message("인증코드 메일 발송 성공")
                .build());
    }

    // 인증코드 검증
    @Operation(summary = "이메일 인증코드 검증", description = "인증코드가 유효한지 검증")
    @PostMapping("/verify")
    public ResponseEntity<Response<Void>> verify(@Valid @RequestBody VerifyRequest request) {
        String userEmail = request.getEmail() + "@kangwon.ac.kr";

        authService.verifyAuthCode(userEmail, request.getVerifyCode().toUpperCase());

        return ResponseEntity.ok(Response.<Void>builder()
                .message("인증 성공")
                .build());
    }
}
