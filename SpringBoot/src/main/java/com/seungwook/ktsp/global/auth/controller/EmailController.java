package com.seungwook.ktsp.global.auth.controller;

import com.seungwook.ktsp.global.auth.dto.request.VerifyRequest;
import com.seungwook.ktsp.global.auth.service.AuthService;
import com.seungwook.ktsp.global.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/email")
public class EmailController {

    private final AuthService authService;

    // 인증코드 메일 발송
    @PostMapping("/send")
    public ResponseEntity<Response<Void>> mailSend(@RequestBody VerifyRequest request)
    {
        String userEmail = request.getEmail() + "kangwon.ac.kr";

        authService.sendRegisterEmail(userEmail);

        return ResponseEntity.ok(Response.<Void>builder()
                .success(true)
                .message("인증코드 메일 발송 성공")
                .build());
    }

    // 인증코드 인증
    @PostMapping("/verify")
    public ResponseEntity<Response<Void>> verify(@RequestBody VerifyRequest request)
    {
        authService.verifyEmailCode(request.getEmail(), request.getVerifyCode());

        return ResponseEntity.ok(Response.<Void>builder()
                .success(true)
                .message("인증코드 메일 발송 성공")
                .build());
    }
}
