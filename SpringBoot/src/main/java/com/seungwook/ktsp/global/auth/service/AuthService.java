package com.seungwook.ktsp.global.auth.service;

import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.domain.user.service.UserDomainService;
import com.seungwook.ktsp.global.auth.dto.UserSession;
import com.seungwook.ktsp.global.auth.dto.request.LoginRequest;
import com.seungwook.ktsp.global.auth.dto.response.LoginResponse;
import com.seungwook.ktsp.global.auth.exception.LoginFailedException;
import com.seungwook.ktsp.global.auth.exception.UserContextException;
import com.seungwook.ktsp.global.auth.support.SessionSecuritySupport;
import com.seungwook.ktsp.global.auth.utils.IpUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String REMEMBER_ME_COOKIE = "REMEMBER_ME";

    private final UserDomainService userDomainService;
    private final SessionRegistry sessionRegistry;
    private final PasswordEncoder passwordEncoder;
    private final RememberMeTokenService rememberMeTokenService;
    private final SessionSecuritySupport sessionSecuritySupport;

    // 요청 사용자 식별 메서드
    @Transactional(readOnly = true)
    public long getUserId() {
        // 현재 요청의 SecurityContext에서 인증 객체 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 객체가 없거나 인증되지 않은 경우 예외 발생
        if (authentication == null || !authentication.isAuthenticated())
            throw new UserContextException("로그인이 필요합니다.");

        // 인증 객체의 principal이 UserSession이 아닐 경우 예외 발생 (타입 불일치 방어)
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserSession session))
            throw new UserContextException("잘못된 인증 세션입니다.");

        return session.getId();
    }

    // 로그인
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {

        // 사용자 인증 및 계정 활성화 여부 검증
        User user = authenticate(request);

        // 기존 세션이 존재하면 무효화
        sessionSecuritySupport.invalidateExistingSession(httpRequest);

        // 자동 로그인 여부 확인
        if (request.isRememberMe()) rememberMe(user.getId(), httpRequest, httpResponse);

        // 인증 세션 객체(UserSession) 생성
        UserSession userSession = sessionSecuritySupport.createUserSession(user, false);

        // UserSession으로 Spring Security 인증 객체 생성
        Authentication authObject = sessionSecuritySupport.buildAuthObject(userSession);

        // SecurityContextHolder에 인증 정보 설정
        SecurityContext context = sessionSecuritySupport.setSecurityContext(authObject);

        // 중복 로그인 방지
        boolean isDuplicatedLogin = sessionSecuritySupport.handleDuplicateSessions(userSession);

        // 새로운 세션 등록
        sessionSecuritySupport.registerNewSession(httpRequest, userSession, context);

        log.info("로그인 성공 - userId: {}({})", user.getId(), IpUtil.getClientIP(httpRequest));
        return new LoginResponse(isDuplicatedLogin);
    }

    // 로그아웃
    public void logout(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        // 현재 SecurityContext 초기화
        SecurityContextHolder.clearContext();

        // 세션 무효화
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            sessionRegistry.removeSessionInformation(session.getId());
            session.invalidate();
        }

        // Remember Me 토큰 처리
        handleRememberMeLogout(httpRequest, httpResponse);
    }

    // 사용자 인증 및 계정 활성화 여부 검증
    private User authenticate(LoginRequest request) {
        User user = userDomainService.findByStudentNumberExceptWithdrawn(request.getStudentNumber())
                .orElseThrow(LoginFailedException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) throw new LoginFailedException();

        if (!user.isActive()) {
            log.warn("비활성 회원 로그인 시도 차단 - userId: {}", user.getId());
            throw new LoginFailedException("정지된 계정입니다. 관리자에게 문의바랍니다.");
        }

        return user;
    }

    // 자동 로그인 설정
    private void rememberMe(long userId, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {

        String token = rememberMeTokenService.createToken(userId, IpUtil.getClientIP(httpRequest));

        ResponseCookie cookie = createRememberMeCookieBuilder(token)
                .maxAge(Duration.ofDays(14)) // 쿠키 만료시간(14일)
                .build();

        httpResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    // RememberMe 쿠키 삭제
    private void handleRememberMeLogout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (REMEMBER_ME_COOKIE.equals(cookie.getName())) {
                    rememberMeTokenService.invalidateToken(cookie.getValue());

                    // 쿠키 삭제
                    ResponseCookie deleteCookie = createRememberMeCookieBuilder("")
                            .maxAge(0) // 쿠키 만료 처리
                            .build();

                    response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
                    break;
                }
            }
        }
    }

    // RememberMe 쿠키 기본 설정
    private ResponseCookie.ResponseCookieBuilder createRememberMeCookieBuilder(String value) {
        return ResponseCookie.from(REMEMBER_ME_COOKIE, value)
                .httpOnly(true) // JS에서 조작 불가
                .secure(true) // HTTPS에서만 적용
                .path("/") // 도메인의 모든 하위 경로 요청에 쿠키 포함
                .sameSite("None"); // 크로스 도메인 전송 허용
        }
}
