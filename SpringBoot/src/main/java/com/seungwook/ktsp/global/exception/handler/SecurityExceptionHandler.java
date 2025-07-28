package com.seungwook.ktsp.global.exception.handler;

import com.seungwook.ktsp.global.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.seungwook.ktsp.global.exception.utils.ErrorUtil.buildErrorResponse;
import static com.seungwook.ktsp.global.exception.utils.ErrorUtil.generateErrorId;

@Order(3)
@Slf4j
@RestControllerAdvice
public class SecurityExceptionHandler {

    // @PreAuthorize 등에서 권한이 부족해 접근이 거부된 경우 처리
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException exception, HttpServletRequest request) {
        String errorId = generateErrorId();

        log.warn("{} - 접근 거부: {} {} | Message: {}", errorId, request.getMethod(), request.getRequestURI(), exception.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(buildErrorResponse("접근 권한이 없습니다.", errorId));
    }
}
