package com.zeroone.ktsp.config;

import com.zeroone.ktsp.domain.User;
import com.zeroone.ktsp.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler
{

    private final HttpSession session;
    private final UserRepository userRepository;

    public CustomAuthenticationSuccessHandler(HttpSession session, UserRepository userRepository)
    {
        this.session = session;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException
    {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal(); // Authentication에서 UserDetails 추출

        User user = userRepository.findByStudentNumber(userDetails.getUsername()).orElseThrow(() -> new IllegalStateException("사용자 정보를 찾을 수 없습니다.")); // 사용자 정보를 DB에서 조회

        session.setAttribute("user", user); // 세션에 사용자 정보 저장
        log.info("로그인 성공 - 이름 : {}, 학번 : {}", user.getName(), authentication.getName());

        response.sendRedirect("/"); // 인증 성공 후 리다이렉트
    }
}