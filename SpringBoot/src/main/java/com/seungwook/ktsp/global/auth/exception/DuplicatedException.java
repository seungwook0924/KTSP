package com.seungwook.ktsp.global.auth.exception;

import com.seungwook.ktsp.global.exception.BaseCustomException;
import org.springframework.http.HttpStatus;

public class DuplicatedException extends BaseCustomException {
    public DuplicatedException (String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
