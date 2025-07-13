package com.seungwook.ktsp.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseCustomException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final int code;

    public BaseCustomException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.code = httpStatus.value();
    }
}

