package com.seungwook.ktsp.domain.user.exception;

import com.seungwook.ktsp.global.exception.BaseCustomException;
import org.springframework.http.HttpStatus;

public class RememberMeAccessDeniedException extends BaseCustomException {
    public RememberMeAccessDeniedException() {
        super(HttpStatus.FORBIDDEN, "자동 로그인 상태에서는 해당 요청이 허용되지 않습니다.");
    }
}

