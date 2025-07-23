package com.seungwook.ktsp.domain.board.common.file.exception;

import com.seungwook.ktsp.global.exception.BaseCustomException;
import org.springframework.http.HttpStatus;

public class FileException extends BaseCustomException {
    public FileException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
