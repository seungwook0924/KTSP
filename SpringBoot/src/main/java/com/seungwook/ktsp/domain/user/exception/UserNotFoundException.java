package com.seungwook.ktsp.domain.user.exception;

import com.seungwook.ktsp.global.exception.BaseCustomException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BaseCustomException {

    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
    }

    public UserNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}

