package com.seungwook.ktsp.global.auth.service;

import com.seungwook.ktsp.global.auth.exception.EmailVerifyException;
import com.seungwook.ktsp.global.auth.utils.IpUtil;
import com.seungwook.ktsp.global.auth.utils.MaskingUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final AuthCodeRedisService authCodeRedisService;
    private final TemplateEngine mailTemplateEngine;

    @Value("${spring.mail.username}")
    private String senderEmail;

    // 인증코드 이메일 발송
    public void sendVerificationEmail(String email, String authCode, HttpServletRequest httpServletRequest) throws MessagingException {

        String clientIP = IpUtil.getClientIP(httpServletRequest);

        // 쿨다운이 적용되었는지 확인
        if (authCodeRedisService.isInCooldown(email, clientIP)) {
            throw new EmailVerifyException(HttpStatus.TOO_MANY_REQUESTS, "인증코드 발송은 3분에 1회만 가능합니다.");
        }

        // 쿨다운 - 180초간 재요청 거부
        authCodeRedisService.setCooldown(email, clientIP, 180);

        // 기존 보안코드가 있다면 삭제하고 재생성
        if (authCodeRedisService.existAuthCode(email)) authCodeRedisService.deleteAuthCode(email);

        // 이메일 폼 생성
        MimeMessage emailForm = createEmailForm(email, authCode);

        // Redis 에 인증 코드 저장(TTL: 5분)
        authCodeRedisService.setAuthCode(email, authCode, 60 * 5L);

        // 이메일 발송
        javaMailSender.send(emailForm);
        log.info("인증코드 이메일 발송 성공 - email: {}", MaskingUtil.maskEmail(email));
    }

    // 이메일 내용 초기화
    private String setContext(Map<String, Object> variables) {
        Context context = new Context();
        variables.forEach(context::setVariable);
        return mailTemplateEngine.process("auth_code", context);
    }

    // 회원가입용 이메일 폼 생성
    private MimeMessage createEmailForm(String email, String authCode) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("KNU 재능공유 플랫폼 인증번호");
        message.setFrom(senderEmail);

        // 템플릿 변수 설정
        Map<String, Object> variables = new HashMap<>();
        variables.put("code", authCode);

        message.setText(setContext(variables), "utf-8", "html");

        return message;
    }
}