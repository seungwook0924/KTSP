package com.seungwook.ktsp.global.auth.controller;

import com.seungwook.ktsp.global.auth.dto.request.VerifyRequest;
import com.seungwook.ktsp.global.auth.service.VerificationService;
import com.seungwook.ktsp.global.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Email Verification", description = "이메일 인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/verify")
public class EmailVerifyController {

    private final VerificationService verificationService;

    // 인증코드 발송
    @Operation(summary = "이메일 인증코드 발송", description = "해당 이메일로 인증코드가 발송됨")
    @PostMapping("/{email}/send")
    public ResponseEntity<Response<Void>> mailSend(
            @Parameter(description = "이메일 앞자리(@kangwon.ac.kr가 자동으로 붙음)", example = "user123")
            @Pattern(regexp = "^[a-zA-Z0-9._%+-]+$", message = "이메일 형식이 올바르지 않습니다.")
            @PathVariable String email) throws MessagingException {

        String userEmail = email + "@kangwon.ac.kr";

        verificationService.sendAuthCode(userEmail);

        return ResponseEntity.ok(Response.<Void>builder()
                .message("인증코드 메일 발송 성공")
                .build());
    }

    // 인증코드 검증
    @Operation(summary = "이메일 인증코드 검증", description = "인증코드가 유효한지 검증")
    @PostMapping
    public ResponseEntity<Response<Void>> verify(@Valid @RequestBody VerifyRequest request) {
        String userEmail = request.getEmail() + "@kangwon.ac.kr";

        verificationService.verifyAuthCode(userEmail, request.getVerifyCode().toUpperCase());

        return ResponseEntity.ok(Response.<Void>builder()
                .message("인증 성공")
                .build());
    }
}
