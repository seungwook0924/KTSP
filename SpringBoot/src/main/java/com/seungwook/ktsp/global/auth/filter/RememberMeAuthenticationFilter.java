package com.seungwook.ktsp.global.auth.filter;

import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.domain.user.repository.UserRepository;
import com.seungwook.ktsp.global.auth.dto.UserSession;
import com.seungwook.ktsp.global.auth.service.RememberMeTokenService;
import com.seungwook.ktsp.global.auth.support.SessionSecuritySupport;
import com.seungwook.ktsp.global.auth.utils.IpUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class RememberMeAuthenticationFilter extends OncePerRequestFilter {

    private final RememberMeTokenService tokenService;
    private final UserRepository userRepository;
    private final SessionSecuritySupport sessionSecuritySupport;

    @Override
    protected void doFilterInternal(HttpServletRequest httpRequest, HttpServletResponse httpResponse, FilterChain filterChain) throws ServletException, IOException {

        // 현재 SecurityContext에 인증 정보가 없을 경우에만 실행
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = extractTokenFromCookie(httpRequest);

            // 쿠키에서 REMEMBER_ME 토큰을 추출
            if (token != null) {

                // 토큰을 통해 userId 조회
                Long userId = tokenService.getUserIdByToken(token, IpUtil.getClientIP(httpRequest));

                if (userId != null) {
                    // userId DB 조회, 계정이 활성 상태인지 확인
                    userRepository.findByIdExceptWithdrawn(userId)
                            .filter(User::isActive)
                            .ifPresent(user -> {

                                // 기존 세션이 존재하면 무효화
                                sessionSecuritySupport.invalidateExistingSession(httpRequest);

                                // 인증 세션 객체(UserSession) 생성
                                UserSession userSession = sessionSecuritySupport.createUserSession(user, true);

                                // UserSession으로 Spring Security 인증 객체 생성
                                Authentication authObject = sessionSecuritySupport.buildAuthObject(userSession);

                                // SecurityContextHolder에 인증 정보 설정
                                SecurityContext context = sessionSecuritySupport.setSecurityContext(authObject);

                                // 중복 로그인 방지
                                sessionSecuritySupport.handleDuplicateSessions(userSession);

                                // 새로운 세션 등록
                                sessionSecuritySupport.registerNewSession(httpRequest, userSession, context);
                            });
                }
            }
        }

        filterChain.doFilter(httpRequest, httpResponse);
    }

    // REMEMBER_ME 쿠키에서 토큰을 추출
    private String extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if ("REMEMBER_ME".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
