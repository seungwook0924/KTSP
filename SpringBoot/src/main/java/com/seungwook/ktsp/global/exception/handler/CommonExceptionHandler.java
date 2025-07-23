package com.seungwook.ktsp.global.exception.handler;

import com.seungwook.ktsp.global.exception.BaseCustomException;
import com.seungwook.ktsp.global.response.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.servlet.NoHandlerFoundException;

import static com.seungwook.ktsp.global.exception.utils.ErrorUtil.buildErrorResponse;
import static com.seungwook.ktsp.global.exception.utils.ErrorUtil.generateErrorId;
import static org.springframework.http.HttpStatus.*;

@Order(100) // 가장 마지막 우선순위
@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    // BaseCustomException 상속한 따로 정의한 예외처리
    @ExceptionHandler(BaseCustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(BaseCustomException exception) {
        String errorId = generateErrorId();
        log.warn("{} - {} | {} | {}\n", errorId, exception.getHttpStatus(), exception.getMessage(), exception.getClass().getSimpleName());

        return ResponseEntity.status(exception.getHttpStatus())
                .body(buildErrorResponse(exception.getMessage(), errorId));
    }

    // 지원하지 않는 경로에 대한 예외처리
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException exception, HttpServletRequest request) {
        String errorId = generateErrorId();

        log.warn("{} - No Handler Found : {} {}", errorId, request.getMethod(), request.getRequestURI());

        return ResponseEntity.status(NOT_FOUND)
                .body(buildErrorResponse("요청한 경로를 찾을 수 없습니다.", errorId));
    }

    // ContentType(JSON 구조)에 대한 예외 처리
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonException(HttpMessageNotReadableException exception) {
        String errorId = generateErrorId();

        log.warn("{} - ContentType Exception : {}\n", exception.getMessage(), errorId);

        return ResponseEntity.status(BAD_REQUEST)
                .body(buildErrorResponse("ContentType 값이 올바르지 않습니다.", errorId));
    }

    // 지원하지 않는 HTTP method를 사용할 시 예외처리
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleRequestMethodException(HttpRequestMethodNotSupportedException exception) {
        String errorId = generateErrorId();

        log.warn("{} - Http Method Not Supported : {}\n", errorId, exception.getMessage());

        return ResponseEntity.status(METHOD_NOT_ALLOWED)
                .body(buildErrorResponse("지원하지 않는 HTTP Method 입니다.", errorId));
    }

    // getReferenceById()로 반환된 프록시 객체가 실제 DB 접근 시 대상 엔티티가 존재하지 않을 경우 발생하는 예외 처리
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest request) {
        String errorId = generateErrorId();
        log.warn("{} - EntityNotFoundException: {} {} | {}", errorId, request.getMethod(), request.getRequestURI(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildErrorResponse("요청한 리소스를 찾을 수 없습니다.", errorId));
    }

    // 외부 리소스 접근 실패 (RestTemplate, WebClient 등)
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ErrorResponse> handleResourceAccessException(ResourceAccessException ex, HttpServletRequest request) {
        String errorId = generateErrorId();

        log.error("{} - 외부 리소스 접근 실패: {} {} | Message: {}", errorId, request.getMethod(), request.getRequestURI(), ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
                .body(buildErrorResponse("서버에 문제가 발생했습니다.", errorId));
    }


    // 처리되지 않은 예외를 최종적으로 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> unhandledException(Exception exception, HttpServletRequest request) {
        String errorId = generateErrorId();

        log.error("{} - UnhandledException: {} {} | Exception: {} | Message: {}",
                errorId,
                request.getMethod(),
                request.getRequestURI(),
                exception.getClass().getSimpleName(),
                exception.getMessage()
        );

        return ResponseEntity.internalServerError()
                .body(buildErrorResponse("서버에 문제가 발생했습니다.", errorId));
    }
}
