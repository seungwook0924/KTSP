package com.seungwook.ktsp.global.auth.service;

import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.domain.user.repository.UserRepository;
import com.seungwook.ktsp.global.auth.dto.UserSession;
import com.seungwook.ktsp.global.auth.exception.UserContextException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    // 요청 사용자 식별 메서드
    @Transactional(readOnly = true)
    public User getUser() {
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

        // 세션에 저장된 학번으로 사용자 조회, 존재하지 않으면 예외 발생
        return userRepository.findByStudentNumber(session.getStudentNumber())
                .orElseThrow(() -> new UserContextException("사용자를 찾을 수 없습니다."));
    }
}
