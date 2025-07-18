package com.seungwook.ktsp.domain.user.exception;

import com.seungwook.ktsp.global.exception.BaseCustomException;
import org.springframework.http.HttpStatus;

public class PasswordMismatchException extends BaseCustomException {
    public PasswordMismatchException() {
        super(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
    }
}
