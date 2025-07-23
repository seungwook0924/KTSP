package com.seungwook.ktsp.domain.board.common.comment.exception;

import com.seungwook.ktsp.global.exception.BaseCustomException;
import org.springframework.http.HttpStatus;

public class CommentNotFoundException extends BaseCustomException {
    public CommentNotFoundException() {
        super(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다.");
    }
}