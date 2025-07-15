package com.seungwook.ktsp.global.auth.exception;

import com.seungwook.ktsp.global.exception.BaseCustomException;
import org.springframework.http.HttpStatus;

public class RegisterFailedException extends BaseCustomException {

    public RegisterFailedException(String message) {
        super(HttpStatus.CONFLICT, message);
    }

    public RegisterFailedException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
