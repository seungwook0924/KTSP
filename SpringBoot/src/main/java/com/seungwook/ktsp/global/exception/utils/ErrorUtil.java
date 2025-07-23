package com.seungwook.ktsp.global.exception.utils;

import com.seungwook.ktsp.global.response.ErrorResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorUtil {

    // 공통 errorId 생성 메서드
    public static String generateErrorId() {
        return java.util.UUID.randomUUID().toString().substring(0, 8);
    }

    // 공통 응답 생성 메서드
    public static ErrorResponse buildErrorResponse(String message, String errorId) {
        return ErrorResponse.builder()
                .message(message)
                .errorId(errorId)
                .build();
    }
}
