package com.seungwook.ktsp.domain.user.exception;

import com.seungwook.ktsp.global.exception.BaseCustomException;
import org.springframework.http.HttpStatus;

public class UserUpdateFailedException extends BaseCustomException {
    public UserUpdateFailedException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
