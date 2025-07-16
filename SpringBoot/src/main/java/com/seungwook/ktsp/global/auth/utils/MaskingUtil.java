package com.seungwook.ktsp.global.auth.utils;

public class MaskingUtil {

    public static String maskEmail(String email) {
        int at = email.indexOf('@');
        if (at < 4) return "****" + email.substring(at); // 4글자 미만이면 전부 마스킹
        return "****" + email.substring(4);
    }
}
