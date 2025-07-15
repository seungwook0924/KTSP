package com.seungwook.ktsp.global.auth.exception;

import com.seungwook.ktsp.global.exception.BaseCustomException;
import org.springframework.http.HttpStatus;

public class StudentNumberException extends BaseCustomException {
    public StudentNumberException () {
        super(HttpStatus.BAD_REQUEST, "학번 형식이 잘못되었습니다.");
    }
}

