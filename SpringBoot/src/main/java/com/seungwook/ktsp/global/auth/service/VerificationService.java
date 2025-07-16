package com.seungwook.ktsp.global.auth.service;

import com.seungwook.ktsp.global.auth.exception.EmailVerifyException;
import com.seungwook.ktsp.global.auth.utils.MaskingUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationService {

    private final EmailService emailService;
    private final AuthCodeRedisService authCodeRedisService;

    // 인증코드 허용 문자 집합
    private final char[] ALPHA_NUMERIC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    // SecureRandom
    private static final SecureRandom RANDOM = new SecureRandom();

    // 인증 코드 발송
    public void sendAuthCode(String email, HttpServletRequest httpServletRequest) throws MessagingException {
        checkEmailDomain(email);
        emailService.sendVerificationEmail(email, generateVerifyCode(), httpServletRequest);
    }

    // 인증코드 검증
    public void verifyAuthCode(String email, String code) {

        checkEmailDomain(email);

        String authCode = authCodeRedisService.getAuthCode(email);

        if (authCode == null) {
            throw new EmailVerifyException("인증번호 발송을 먼저 요청하세요.");
        }

        if (!authCode.equals(code)) {
            log.warn("인증코드 검증 실패 - email: {}", MaskingUtil.maskEmail(email));

            // 실패 횟수 증가
            authCodeRedisService.incrementFailCount(email);

            // 실패 횟수 5회 이상 -> 레디스에서 인증코드 삭제
            int failCount = authCodeRedisService.getFailCount(email);
            if(failCount >= 5) {
                authCodeRedisService.deleteAuthCode(email);
                throw new EmailVerifyException("이메일 인증을 5회 실패하였습니다. 인증번호 발송을 다시 해주세요.");
            }

            throw new EmailVerifyException("현재 " + failCount + "회 잘못된 인증번호를 입력하였습니다.");
        }

        // 인증 성공시 레디스에서 인증코드 삭제
        authCodeRedisService.deleteAuthCode(email);

        // 이메일 인증 성공여부 저장(TTL: 60분)
        authCodeRedisService.setEmailAsVerified(email, 60 * 60L);
        log.info("인증코드 검증 성공 - email: {}", MaskingUtil.maskEmail(email));
    }

    // 인증코드 생성
    private String generateVerifyCode() {
        int verifyCodeLength = 6;

        StringBuilder sb = new StringBuilder(verifyCodeLength);
        for (int i = 0; i < verifyCodeLength; i++) {
            char c = ALPHA_NUMERIC[RANDOM.nextInt(ALPHA_NUMERIC.length)];
            sb.append(c);
        }

        return sb.toString();
    }

    // 강원대학교 이메일인지 검증
    private void checkEmailDomain(String email) {
        if (!email.endsWith("@kangwon.ac.kr"))
            throw new EmailVerifyException(HttpStatus.BAD_REQUEST, "강원대학교 이메일이 아닙니다.");
    }


}
