package com.zeroone.ktsp.controller;

import com.zeroone.ktsp.DTO.RegisterDTO;
import com.zeroone.ktsp.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
public class EmailController {
    private final EmailService emailService;

    // 인증코드 메일 발송
    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> mailSend(@RequestBody RegisterDTO registerDTO) throws MessagingException {
        String userEmailId = registerDTO.getEmail();
        if (registerDTO.getEmail() == null || userEmailId.substring(0, userEmailId.indexOf("@")).trim().isEmpty())
        {
            throw new IllegalArgumentException("이메일 앞부분이 null 처리되었음.");
        }
        emailService.sendEmail(registerDTO.getEmail());
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    // 인증코드 인증
    @PostMapping("/verify")
    public ResponseEntity<Map<String, String>> verify(@RequestBody RegisterDTO registerDTO) {
        boolean isVerify = emailService.verifyEmailCode(registerDTO.getEmail(), registerDTO.getVerifyCode());
        Map<String, String> response = new HashMap<>();

        if (isVerify)
        {
            response.put("status", "success");
            return ResponseEntity.ok(response);
        }
        else
        {
            response.put("status", "fail");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
