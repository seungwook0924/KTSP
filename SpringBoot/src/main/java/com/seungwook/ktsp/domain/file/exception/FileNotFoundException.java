package com.seungwook.ktsp.domain.file.exception;

import com.seungwook.ktsp.global.exception.BaseCustomException;
import org.springframework.http.HttpStatus;

public class FileNotFoundException extends BaseCustomException {
    public FileNotFoundException() {
        super(HttpStatus.BAD_REQUEST, "파일을 찾을 수 없습니다.");
    }
}