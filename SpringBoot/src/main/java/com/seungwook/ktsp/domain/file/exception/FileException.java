package com.seungwook.ktsp.domain.file.exception;

import com.seungwook.ktsp.global.exception.BaseCustomException;
import org.springframework.http.HttpStatus;

public class FileException extends BaseCustomException {
    public FileException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    public FileException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
