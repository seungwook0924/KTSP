package com.seungwook.ktsp.global.auth.exception;

import com.seungwook.ktsp.global.exception.BaseCustomException;
import org.springframework.http.HttpStatus;

public class VerifyCodeException extends BaseCustomException {
    public VerifyCodeException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
