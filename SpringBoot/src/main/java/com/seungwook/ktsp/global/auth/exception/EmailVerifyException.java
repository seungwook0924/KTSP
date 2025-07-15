package com.seungwook.ktsp.global.auth.exception;

import com.seungwook.ktsp.global.exception.BaseCustomException;
import org.springframework.http.HttpStatus;

public class EmailVerifyException extends BaseCustomException {
    public EmailVerifyException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    public EmailVerifyException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
