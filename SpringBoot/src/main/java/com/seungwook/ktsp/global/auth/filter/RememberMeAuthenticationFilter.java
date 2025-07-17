package com.seungwook.ktsp.global.auth.filter;

import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.domain.user.repository.UserRepository;
import com.seungwook.ktsp.global.auth.dto.UserSession;
import com.seungwook.ktsp.global.auth.service.RememberMeTokenService;
import com.seungwook.ktsp.global.auth.utils.IpUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class RememberMeAuthenticationFilter extends OncePerRequestFilter {

    private final RememberMeTokenService tokenService;
    private final UserRepository userRepository;
    private final SessionRegistry sessionRegistry;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 인증 정보가 있는지 검사
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = extractTokenFromCookie(request);

            // RememberMe 토큰이 있는지 검사
            if (token != null) {
                Long userId = tokenService.getUserIdByToken(token, IpUtil.getClientIP(request));
                User user = userRepository.findById(userId).orElse(null);

                // 사용자가 활성 상태인지 검사
                if (user != null && user.getActivated()) {

                    // UserSession 생성(isRememberMe=true)
                    UserSession sessionUser = new UserSession(user.getId(), user.getStudentNumber(), user.getRole(), true);


                    // 인증 객체 생성
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            sessionUser,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                    );

                    // SecurityContextHolder에 인증 정보 설정
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    context.setAuthentication(authentication);
                    SecurityContextHolder.setContext(context);

                    // 새 세션 생성 후 인증 정보 및 SecurityContext 저장
                    HttpSession newSession = request.getSession(true);
                    sessionRegistry.registerNewSession(newSession.getId(), sessionUser);
                    newSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    // HttpServletRequest에서 REMEMBER_ME 쿠키가 있는지 검사
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
