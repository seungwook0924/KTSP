package com.seungwook.ktsp.global.auth.exception;

import com.seungwook.ktsp.global.exception.BaseCustomException;
import org.springframework.http.HttpStatus;

public class UserContextException extends BaseCustomException {

    public UserContextException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
