package com.seungwook.ktsp.global.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seungwook.ktsp.global.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.io.IOException;

// 403 상태코드 반환 (권한 부족)
@Component
@RequiredArgsConstructor
public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Response<Void> errorResponse = Response.<Void>builder()
                .success(false)
                .message("접근 권한 부족")
                .build();

        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}