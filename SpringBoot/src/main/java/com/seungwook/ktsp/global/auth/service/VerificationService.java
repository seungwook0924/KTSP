package com.seungwook.ktsp.global.auth.service;

import com.seungwook.ktsp.global.auth.exception.EmailVerifyException;
import jakarta.mail.MessagingException;
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
    private static final char[] ALPHA_NUMERIC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    // SecureRandom
    private static final SecureRandom RANDOM = new SecureRandom();

    // 인증 코드 발송
    public void sendAuthCode(String email) throws MessagingException {
        checkEmailDomain(email);
        emailService.sendVerificationEmail(email, generateVerifyCode());
    }

    // 인증코드 검증
    public void verifyAuthCode(String email, String code) {

        checkEmailDomain(email);

        String authCode = authCodeRedisService.getAuthCode(email);

        if (authCode == null) {
            throw new EmailVerifyException("인증번호 발송을 먼저 요청하세요.");
        }

        if (!authCode.equals(code)) {
            log.warn("인증코드 검증 실패 - email: {}, 예상 값: {}, 입력 값: {}", email, authCode, code);
            authCodeRedisService.incrementFailCount(email); // 실패 횟수 증가

            int failCount = authCodeRedisService.getFailCount(email);
            if(failCount >= 5) {
                authCodeRedisService.deleteAuthCode(email);
                throw new EmailVerifyException("이메일 인증을 5회 실패하였습니다. 인증번호 발송을 다시 해주세요.");
            }

            throw new EmailVerifyException("현재 " + failCount + "회 잘못된 인증번호를 입력하였습니다.");
        }

        // 인증 성공시 레디스에서 키 삭제
        authCodeRedisService.deleteAuthCode(email);

        // 이메일 인증 성공여부 저장(TTL: 60분)
        authCodeRedisService.setEmailAsVerified(email, 60 * 60L);
        log.info("인증코드 검증 성공 - email: {}", email);
    }

    // 인증코드 생성
    private String generateVerifyCode() {
        int length = 6;

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char c = ALPHA_NUMERIC[RANDOM.nextInt(ALPHA_NUMERIC.length)];
            sb.append(c);
        }

        return sb.toString();
    }

    //
    private void checkEmailDomain(String email) {
        if (!email.endsWith("@kangwon.ac.kr"))
            throw new EmailVerifyException(HttpStatus.BAD_REQUEST, "강원대학교 이메일이 아닙니다.");
    }
}
