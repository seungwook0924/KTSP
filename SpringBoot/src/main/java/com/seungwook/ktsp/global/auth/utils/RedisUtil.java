package com.seungwook.ktsp.global.auth.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final StringRedisTemplate template;
    private static final String VERIFY_CODE_PREFIX = "verify-code:";

    public String getAuthCode(String email) {
        return (String) template.opsForHash().get(getVerifyCodeKey(email), "code");
    }

    public boolean existAuthCode(String email) {
        return template.hasKey(getVerifyCodeKey(email));
    }

    public void setAuthCode(String email, String authCode, long ttlSeconds) {
        String key = getVerifyCodeKey(email);

        Map<String, String> data = new HashMap<>();
        data.put("code", authCode);
        data.put("failCount", "0");

        template.opsForHash().putAll(key, data);
        template.expire(key, Duration.ofSeconds(ttlSeconds));
    }

    public void incrementFailCount(String email) {
        template.opsForHash().increment(getVerifyCodeKey(email), "failCount", 1);
    }

    public int getFailCount(String email) {
        Object count = template.opsForHash().get(getVerifyCodeKey(email), "failCount");
        return Integer.parseInt(count.toString());
    }

    public void deleteAuthCode(String email) {
        template.delete(getVerifyCodeKey(email));
    }

    private String getVerifyCodeKey(String email) {
        return VERIFY_CODE_PREFIX + email;
    }

}
