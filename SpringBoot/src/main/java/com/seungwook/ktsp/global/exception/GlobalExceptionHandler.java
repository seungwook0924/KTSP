package com.seungwook.ktsp.global.exception;

import com.seungwook.ktsp.global.response.ErrorResponse;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Objects;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

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

    // Validation 검증 실패 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleInputFieldException(MethodArgumentNotValidException exception) {
        FieldError mainError = exception.getFieldErrors().getFirst();
        String responseMessage = mainError.getDefaultMessage();
        String errorId = generateErrorId();

        log.warn("{} - Validation 검증 실패: {}\n", errorId, responseMessage);

        return ResponseEntity.status(BAD_REQUEST)
                .body(buildErrorResponse(responseMessage, errorId));
    }

    // @PathVariable, @RequestParam 등 메서드 파라미터 유효성 실패 시 처리
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
        String errorId = generateErrorId();

        String message = ex.getAllErrors().stream()
                .map(MessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse("요청 파라미터 값이 올바르지 않습니다.");

        log.warn("{} - @PathVariable, @RequestParam 검증 실패: {}", errorId, message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildErrorResponse(message, errorId));
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

    // RequestParam이 누락됐을 시 예외처리
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestParamException(MissingServletRequestParameterException exception) {
        String errorId = generateErrorId();

        log.warn("{} - Missing RequestParam Exception : {}\n", errorId, exception.getMessage());

        return ResponseEntity.status(BAD_REQUEST)
                .body(buildErrorResponse("RequestParam이 누락되었습니다.", errorId));
    }

    // 파라미터 타입 불일치로 발생하는 예외 처리
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        String errorId = generateErrorId();

        log.warn("{} - Parameter Type Mismatch : {}\n", errorId, exception.getMessage());

        return ResponseEntity.status(BAD_REQUEST)
                .body(buildErrorResponse("요청 파라미터 값이 올바르지 않습니다.", errorId));
    }

    // 멀티파트 요청에서 필수 Part가 누락됐을 때 발생하는 예외 처리
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestPartException(MissingServletRequestPartException exception) {
        String errorId = generateErrorId();

        log.warn("{} - Missing Servlet Request Part : {}\n", errorId, exception.getMessage());

        return ResponseEntity.status(BAD_REQUEST)
                .body(buildErrorResponse("요청 파라미터 값이 올바르지 않습니다.", errorId));
    }

    // getReferenceById()로 반환된 프록시 객체가 실제 DB 접근 시 대상 엔티티가 존재하지 않을 경우 발생하는 예외 처리
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest request) {
        String errorId = generateErrorId();
        log.warn("{} - EntityNotFoundException: {} {} | {}", errorId, request.getMethod(), request.getRequestURI(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildErrorResponse("요청한 리소스를 찾을 수 없습니다.", errorId));
    }

    // @PreAuthorize 등에서 권한이 부족해 접근이 거부된 경우 처리
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException exception, HttpServletRequest request) {
        String errorId = generateErrorId();

        log.warn("{} - 접근 거부: {} {} | Message: {}", errorId, request.getMethod(), request.getRequestURI(), exception.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(buildErrorResponse("접근 권한이 없습니다.", errorId));
    }

    // 외부 리소스 접근 실패 (RestTemplate, WebClient 등)
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ErrorResponse> handleResourceAccessException(ResourceAccessException ex, HttpServletRequest request) {
        String errorId = generateErrorId();

        log.error("{} - 외부 리소스 접근 실패: {} {} | Message: {}", errorId, request.getMethod(), request.getRequestURI(), ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
                .body(buildErrorResponse("서버에 문제가 발생했습니다.", errorId));
    }

    // 메일 발송 예외 처리
    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ErrorResponse> handleMailException(MessagingException ex) {
        String errorId = generateErrorId();
        log.error("{} - 이메일 발송 실패: {}", errorId, ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildErrorResponse("이메일 발송에 실패했습니다.", errorId));
    }


    // 위에서 처리되지 않은 예외를 최종적으로 처리
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

    // 공통 errorId 생성 메서드
    private String generateErrorId() {
        return java.util.UUID.randomUUID().toString().substring(0, 8);
    }

    // 공통 응답 생성 메서드
    private ErrorResponse buildErrorResponse(String message, String errorId) {
        return ErrorResponse.builder()
                .message(message)
                .errorId(errorId)
                .build();
    }
}
