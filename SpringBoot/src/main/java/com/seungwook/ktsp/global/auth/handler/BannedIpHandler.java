package com.seungwook.ktsp.global.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seungwook.ktsp.global.response.Response;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class BannedIpHandler {

    private final ObjectMapper objectMapper;

    public void handle(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Response<Void> errorResponse = Response.<Void>builder()
                .success(false)
                .message("차단된 아이피 또는 해외 아이피로 접근 불가")
                .build();

        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
