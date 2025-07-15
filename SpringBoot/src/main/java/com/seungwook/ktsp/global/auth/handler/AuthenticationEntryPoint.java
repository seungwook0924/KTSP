package com.seungwook.ktsp.global.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seungwook.ktsp.global.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;

// 401 상태코드 반환 (인증 실패)
@Component
@RequiredArgsConstructor
public class AuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Response<Void> errorResponse = Response.<Void>builder()
                .success(false)
                .message("로그인 필요")
                .build();

        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
