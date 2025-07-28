package com.seungwook.ktsp.global.auth.support;

import com.seungwook.ktsp.domain.user.entity.enums.UserRole;
import com.seungwook.ktsp.global.auth.dto.UserSession;
import com.seungwook.ktsp.global.auth.exception.UserContextException;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthHandler {

    private static final AuthenticationTrustResolver TRUST = new AuthenticationTrustResolverImpl();

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

    // 게시글 관리 권한 보유 여부 리턴
    public static boolean hasManagePermission(long resourceOwnerId) {

        Authentication authentication = extractAuthentication();

        // 로그인 하지 않은 사용자는 관리 권한 없음
        if (isAnonymous(authentication)) return false;

        // 로그인 한 사용자라면 UserSession 추출
        UserSession userSession = extractUserSession(authentication.getPrincipal());

        // 관리 권한 보유 여부 리턴
        return userSession.getRole() == UserRole.ADMIN || userSession.getId() == resourceOwnerId;
    }

    // SecurityContextHolder 에서 UserSession 추출
    private static UserSession extractUserSessionFromSecurityContext() {

        Authentication authentication = extractAuthentication();

        // 인증 객체가 없거나 인증되지 않은 경우 예외 발생
        if (isAnonymous(authentication)) throw new UserContextException("로그인이 필요합니다.");

        return extractUserSession(authentication.getPrincipal());
    }

    // 인증 객체의 principal이 UserSession이 아닐 경우 예외 발생 (타입 불일치 방어)
    private static UserSession extractUserSession(Object principal) {

        // 타입 검사
        if (!(principal instanceof UserSession userSession))
            throw new UserContextException("잘못된 인증 세션입니다.");

        // UserSession 반환
        return userSession;
    }

    // 현재 요청의 SecurityContext에서 인증 객체 추출
    private static Authentication extractAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    // 로그인하지 않은 사용자인지 검사
    private static boolean isAnonymous(Authentication authentication) {
        return authentication == null || TRUST.isAnonymous(authentication) || !authentication.isAuthenticated();
    }
}

