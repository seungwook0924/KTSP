package com.seungwook.ktsp.global.auth.exception;

import com.seungwook.ktsp.global.exception.BaseCustomException;
import org.springframework.http.HttpStatus;

public class LoginFailedException extends BaseCustomException {
    public LoginFailedException() {
        super(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 잘못되었습니다.");
    }

    public LoginFailedException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
