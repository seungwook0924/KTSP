package com.seungwook.ktsp.global.auth.support;

import com.seungwook.ktsp.domain.user.entity.enums.UserRole;
import com.seungwook.ktsp.global.auth.dto.UserSession;
import com.seungwook.ktsp.global.auth.exception.UserContextException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthHandler {

    // UserSession 에서 userId 리턴
    public static long getUserId() {
        UserSession userSession = extractUserSessionFromSecurityContext();
        return userSession.getId();
    }

    // UserSession 에서 UserRole 리턴
    public static UserRole getUserRole() {
        UserSession userSession = extractUserSessionFromSecurityContext();
        return userSession.getRole();
    }

    // SecurityContextHolder 에서 UserSession 추출
    private static UserSession extractUserSessionFromSecurityContext() {

        // 현재 요청의 SecurityContext에서 인증 객체 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 객체가 없거나 인증되지 않은 경우 예외 발생
        if (authentication == null || !authentication.isAuthenticated())
            throw new UserContextException("로그인이 필요합니다.");

        // 인증 객체의 principal이 UserSession이 아닐 경우 예외 발생 (타입 불일치 방어)
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserSession userSession))
            throw new UserContextException("잘못된 인증 세션입니다.");

        return userSession;
    }
}

