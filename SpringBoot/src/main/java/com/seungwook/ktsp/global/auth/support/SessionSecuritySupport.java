package com.seungwook.ktsp.global.auth.support;

import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.global.auth.dto.UserSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionSecuritySupport {

    private final SessionRegistry sessionRegistry;

    // 기존 세션이 존재하면 무효화
    public void invalidateExistingSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    // 인증 세션 객체(UserSession) 생성
    public UserSession createUserSession(User user, boolean isRememberMe) {
        return new UserSession(user.getId(), user.getStudentNumber(), user.getRole(), isRememberMe);
    }

    // UserSession으로 Spring Security 인증 객체 생성
    public Authentication buildAuthObject(UserSession sessionUser) {
        return new UsernamePasswordAuthenticationToken(
                sessionUser,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + sessionUser.getRole().name()))
        );
    }

    // SecurityContextHolder에 인증 정보 설정
    public SecurityContext setSecurityContext(Authentication authObject) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authObject);
        SecurityContextHolder.setContext(context);
        return context;
    }

    // 중복 로그인 방지
    public boolean handleDuplicateSessions(UserSession userSession) {
        List<SessionInformation> sessions = sessionRegistry.getAllSessions(userSession, false);
        if (!sessions.isEmpty()) {
            sessions.forEach(SessionInformation::expireNow);
            log.warn("중복 로그인 감지(기존 세션 만료 처리) - userId: {}", userSession.getId());
            return true;
        }
        return false;
    }

    // 새로운 세션 등록
    public void registerNewSession(HttpServletRequest request, UserSession sessionUser, SecurityContext context) {
        HttpSession newSession = request.getSession(true);
        sessionRegistry.registerNewSession(newSession.getId(), sessionUser);
        newSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
    }
}
