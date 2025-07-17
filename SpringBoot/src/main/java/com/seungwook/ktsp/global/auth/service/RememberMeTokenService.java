package com.seungwook.ktsp.global.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RememberMeTokenService {

    private static final String REMEMBER_ME_PREFIX = "auto-login:";
    private static final long EXPIRE_SECONDS = 7 * 24 * 60 * 60; // 7일

    private final RedisTemplate<String, String> redisTemplate;

    // RememberMe 토큰 생성(userId + IP)
    public String createToken(long userId, String ip) {
        String token = UUID.randomUUID().toString();
        String key = REMEMBER_ME_PREFIX + token;
        String value = userId + ":" + ip;
        redisTemplate.opsForValue().set(key, value, EXPIRE_SECONDS, TimeUnit.SECONDS);
        return token;
    }

    // RememberMe 토큰, IP를 바탕으로 userId 리턴
    public Long getUserIdByToken(String token, String currentIp) {
        String key = REMEMBER_ME_PREFIX + token;
        String storedValue = redisTemplate.opsForValue().get(key);

        if (storedValue == null || !storedValue.contains(":")) return null;

        String[] parts = storedValue.split(":", 2);
        Long userId = Long.parseLong(parts[0]);
        String storedIp = parts[1];

        if (!storedIp.equals(currentIp)) return null;

        return userId;
    }

    public void invalidateToken(String token) {
        redisTemplate.delete(REMEMBER_ME_PREFIX + token);
    }
}
