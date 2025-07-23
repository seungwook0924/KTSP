package com.seungwook.ktsp.global.exception.handler;

import com.seungwook.ktsp.global.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import static com.seungwook.ktsp.global.exception.utils.ErrorUtil.buildErrorResponse;
import static com.seungwook.ktsp.global.exception.utils.ErrorUtil.generateErrorId;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Order(2)
@Slf4j
@RestControllerAdvice
public class FileUploadExceptionHandler {

    // 멀티파트 요청에서 필수 Part가 누락됐을 때 발생하는 예외 처리
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestPartException(MissingServletRequestPartException exception) {
        String errorId = generateErrorId();

        log.warn("{} - Missing Servlet Request Part : {}\n", errorId, exception.getMessage());

        return ResponseEntity.status(BAD_REQUEST)
                .body(buildErrorResponse("요청 파라미터 값이 올바르지 않습니다.", errorId));
    }

    // Multipart/form-data 요청 오류 처리
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ErrorResponse> handleMultipartException(MultipartException exception, HttpServletRequest request) {
        String errorId = generateErrorId();

        log.warn("{} - MultipartException: {} {} | Message: {}",
                errorId,
                request.getMethod(),
                request.getRequestURI(),
                exception.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildErrorResponse("파일 업로드 요청이 올바르지 않습니다.", errorId));
    }

    // 파일 업로드 최대크기 초과 예외 처리
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex, HttpServletRequest request) {
        String errorId = generateErrorId();

        log.warn("{} - MaxUploadSizeExceededException: {} {} | Message: {}",
                errorId,
                request.getMethod(),
                request.getRequestURI(),
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE) // 413
                .body(buildErrorResponse("업로드 가능한 파일 크기를 초과했습니다.", errorId));
    }
}
