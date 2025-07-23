package com.seungwook.ktsp.domain.board.common.comment.exception;

import com.seungwook.ktsp.global.exception.BaseCustomException;
import org.springframework.http.HttpStatus;

public class CommentException extends BaseCustomException {
    public CommentException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}