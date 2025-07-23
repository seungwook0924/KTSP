package com.seungwook.ktsp.global.exception.handler;

import com.seungwook.ktsp.global.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Objects;

import static com.seungwook.ktsp.global.exception.utils.ErrorUtil.buildErrorResponse;
import static com.seungwook.ktsp.global.exception.utils.ErrorUtil.generateErrorId;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Order(1)
@Slf4j
@RestControllerAdvice
public class ValidationExceptionHandler {

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

    // 파라미터 타입 불일치로 발생하는 예외 처리
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        String errorId = generateErrorId();

        log.warn("{} - Parameter Type Mismatch : {}\n", errorId, exception.getMessage());

        return ResponseEntity.status(BAD_REQUEST)
                .body(buildErrorResponse("요청 파라미터 값이 올바르지 않습니다.", errorId));
    }

    // RequestParam이 누락됐을 시 예외처리
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestParamException(MissingServletRequestParameterException exception) {
        String errorId = generateErrorId();

        log.warn("{} - Missing RequestParam Exception : {}\n", errorId, exception.getMessage());

        return ResponseEntity.status(BAD_REQUEST)
                .body(buildErrorResponse("RequestParam이 누락되었습니다.", errorId));
    }
}
