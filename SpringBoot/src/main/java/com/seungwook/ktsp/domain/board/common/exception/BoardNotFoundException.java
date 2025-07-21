package com.seungwook.ktsp.domain.board.common.exception;

import com.seungwook.ktsp.global.exception.BaseCustomException;
import org.springframework.http.HttpStatus;

public class BoardNotFoundException extends BaseCustomException {
    public BoardNotFoundException() {
        super(HttpStatus.NOT_FOUND, "게시글을 수 없습니다.");
    }
}
