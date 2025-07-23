package com.seungwook.ktsp.global.exception.handler;

import com.seungwook.ktsp.global.response.ErrorResponse;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.seungwook.ktsp.global.exception.utils.ErrorUtil.buildErrorResponse;
import static com.seungwook.ktsp.global.exception.utils.ErrorUtil.generateErrorId;

@Order(4)
@Slf4j
@RestControllerAdvice
public class MailExceptionHandler {

    // 메일 발송 예외 처리
    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ErrorResponse> handleMailException(MessagingException ex) {
        String errorId = generateErrorId();
        log.error("{} - 이메일 발송 실패: {}", errorId, ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildErrorResponse("이메일 발송에 실패했습니다.", errorId));
    }
}
