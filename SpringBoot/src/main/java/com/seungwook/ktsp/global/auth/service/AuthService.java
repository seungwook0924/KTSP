package com.seungwook.ktsp.global.auth.service;

import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.domain.user.repository.UserRepository;
import com.seungwook.ktsp.global.auth.dto.UserSession;
import com.seungwook.ktsp.global.auth.dto.request.LoginRequest;
import com.seungwook.ktsp.global.auth.dto.response.LoginResponse;
import com.seungwook.ktsp.global.auth.exception.LoginFailedException;
import com.seungwook.ktsp.global.auth.exception.UserContextException;
import com.seungwook.ktsp.global.auth.utils.IpUtil;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final SessionRegistry sessionRegistry;
    private final PasswordEncoder passwordEncoder;

    // 요청 사용자 식별 메서드
    @Transactional(readOnly = true)
    public long getUserId() {
        // 현재 요청의 SecurityContext에서 인증 객체 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 객체가 없거나 인증되지 않은 경우 예외 발생
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserContextException("로그인이 필요합니다.");
        }

        // 인증 객체의 principal이 UserSession이 아닐 경우 예외 발생 (타입 불일치 방어)
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserSession session)) {
            throw new UserContextException("잘못된 인증 세션입니다.");
        }

        return session.getId();
    }

    // 로그인
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request, HttpServletRequest httpRequest) {

        // 사용자 인증 및 계정 활성화 여부 검증
        User user = authenticate(request);

        // 기존 세션이 존재하면 무효화
        invalidateExistingSession(httpRequest);

        // 인증 세션 객체(UserSession) 생성
        UserSession sessionUser = createUserSession(user);

        // UserSession으로 Spring Security 인증 객체 생성
        Authentication authentication = buildAuthentication(sessionUser);

        // SecurityContextHolder에 인증 정보 설정
        SecurityContext context = setSecurityContext(authentication);

        // 중복 로그인 방지
        boolean isDuplicatedLogin = handleDuplicateSessions(sessionUser, user);

        // 기존 세션이 남아있다면 강제 만료 처리
        registerNewSession(httpRequest, sessionUser, context);

        log.info("로그인 성공 - {}({} / {})", user.getName(), user.getStudentNumber(), IpUtil.getClientIP(httpRequest));
        return new LoginResponse(isDuplicatedLogin);
    }

    // 로그아웃
    public void logout(HttpServletRequest request) {
        // 현재 SecurityContext 초기화
        SecurityContextHolder.clearContext();

        // 세션 무효화
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    // 사용자 인증 및 계정 활성화 여부 검증
    private User authenticate(LoginRequest request) {
        User user = userRepository.findByStudentNumber(request.getStudentNumber())
                .orElseThrow(LoginFailedException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) throw new LoginFailedException();

        if (!user.getActivated()) throw new LoginFailedException("정지된 계정입니다. 관리자에게 문의바랍니다.");

        return user;
    }

    // 기존 세션이 존재하면 무효화
    private void invalidateExistingSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
    }

    // 인증 세션 객체(UserSession) 생성
    private UserSession createUserSession(User user) {
        return new UserSession(user.getId(), user.getEmail(), user.getRole());
    }

    // UserSession으로 Spring Security 인증 객체 생성
    private Authentication buildAuthentication(UserSession sessionUser) {
        return new UsernamePasswordAuthenticationToken(
                sessionUser,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + sessionUser.getRole().name()))
        );
    }

    // SecurityContextHolder에 인증 정보 설정
    private SecurityContext setSecurityContext(Authentication authentication) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        return context;
    }

    // 중복 로그인 방지
    private boolean handleDuplicateSessions(UserSession sessionUser, User user) {
        List<SessionInformation> sessions = sessionRegistry.getAllSessions(sessionUser, false);
        if (!sessions.isEmpty()) {
            sessions.forEach(SessionInformation::expireNow);
            log.info("중복 로그인 감지 - 기존 세션 만료 처리: {}({})", user.getName(), user.getStudentNumber());
            return true;
        }
        return false;
    }

    // 새 세션 생성 후 인증 정보 및 SecurityContext 저장
    private void registerNewSession(HttpServletRequest request, UserSession sessionUser, SecurityContext context) {
        HttpSession newSession = request.getSession(true);
        sessionRegistry.registerNewSession(newSession.getId(), sessionUser);
        newSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
    }
}
