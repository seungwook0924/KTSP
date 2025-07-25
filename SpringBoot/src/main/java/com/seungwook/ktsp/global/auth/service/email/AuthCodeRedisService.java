package com.seungwook.ktsp.global.auth.service.email;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthCodeRedisService {

    private final StringRedisTemplate template;
    private static final String VERIFY_CODE_PREFIX = "verify-code:";
    private static final String VERIFIED_EMAIL_PREFIX = "verified-email:";
    private static final String COOLDOWN_KEY_PREFIX = "verify-cooldown:";
    private static final String IP_COOLDOWN_KEY_PREFIX = "verify-ip-cooldown:";

    // 보안코드 조회
    public String getAuthCode(String email) {
        return (String) template.opsForHash().get(getVerifyCodeKey(email), "code");
    }

    // 보안코드 존재 여부 확인
    public boolean existAuthCode(String email) {
        return template.hasKey(getVerifyCodeKey(email));
    }

    // 보안코드 저장
    public void setAuthCode(String email, String authCode, long ttlSeconds) {
        String key = getVerifyCodeKey(email);

        Map<String, String> data = new HashMap<>();
        data.put("code", authCode);
        data.put("failCount", "0");

        template.opsForHash().putAll(key, data);
        template.expire(key, Duration.ofSeconds(ttlSeconds));
    }

    // 인증 실패횟수 증가
    public void incrementFailCount(String email) {
        template.opsForHash().increment(getVerifyCodeKey(email), "failCount", 1);
    }

    // 인증 실패횟수 조회
    public int getFailCount(String email) {
        Object count = template.opsForHash().get(getVerifyCodeKey(email), "failCount");
        return Integer.parseInt(Objects.requireNonNull(count).toString());
    }

    // 보안코드 제거
    public void deleteAuthCode(String email) {
        template.delete(getVerifyCodeKey(email));
    }

    // 보안코드 관련 편의 메서드
    private String getVerifyCodeKey(String email) {
        return VERIFY_CODE_PREFIX + email;
    }


    // 이메일 인증 성공 저장
    public void setEmailAsVerified(String email, long ttlSeconds) {
        String key = getVerifiedEmailKey(email);
        template.opsForValue().set(key, "", Duration.ofSeconds(ttlSeconds));
    }

    // 인증 여부 조회
    public boolean existVerifiedEmail(String email) {
        return template.hasKey(getVerifiedEmailKey(email));
    }

    // 인증 완료 이메일 제거
    public void deleteVerifiedEmail(String email) {
        template.delete(getVerifiedEmailKey(email));
    }

    // 이메일 인증 성공관련 편의 메서드
    private String getVerifiedEmailKey(String email) {
        return VERIFIED_EMAIL_PREFIX + email;
    }


    // (이메일 + IP)쿨다운 기간인지 확인
    public boolean isInCooldown(String email, String ip) {
        return template.hasKey(getEmailCooldownKey(email)) || template.hasKey(getIpCooldownKey(ip));
    }

    // (이메일 + IP)쿨다운 설정
    public void setCooldown(String email, String ip, long ttlSeconds) {
        template.opsForValue().set(getEmailCooldownKey(email), "1", Duration.ofSeconds(ttlSeconds));
        template.opsForValue().set(getIpCooldownKey(ip), "1", Duration.ofSeconds(ttlSeconds));
    }

    // 이메일 쿨다운 여부
    private String getEmailCooldownKey(String email) {
        return COOLDOWN_KEY_PREFIX + email;
    }

    // IP 쿨다운 여부
    private String getIpCooldownKey(String ip) {
        return IP_COOLDOWN_KEY_PREFIX + ip;
    }
}
