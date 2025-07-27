package com.seungwook.ktsp.global.auth.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IpUtil {
    private static final String HDR_CF_CONNECTING_IP = "CF-Connecting-IP";
    private static final String HDR_X_FORWARDED_FOR  = "X-Forwarded-For";

    /**
     * 실제 클라이언트 IP 반환한다.
     * 1) CF-Connecting-IP (신뢰 가능)
     * 2) X-Forwarded-For의 첫 번째 IP
     * 3) request.getRemoteAddr() (Cloudflare 엣지 IP)
     */
    public static String getClientIP(HttpServletRequest request) {

        String ip = firstNonEmpty(request.getHeader(HDR_CF_CONNECTING_IP));

        if (ip == null) ip = firstNonEmpty(firstFromXff(request.getHeader(HDR_X_FORWARDED_FOR)));

        if (ip == null) ip = request.getRemoteAddr();

        return ip;
    }

    private static String firstFromXff(String xff) {

        if (xff == null || xff.isBlank()) return null;

        int comma = xff.indexOf(',');

        return (comma == -1 ? xff : xff.substring(0, comma)).trim();
    }

    private static String firstNonEmpty(String v) {
        if (v == null) return null;
        v = v.trim();
        if (v.isEmpty() || "unknown".equalsIgnoreCase(v)) return null;
        return v;
    }
}
