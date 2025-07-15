package com.seungwook.ktsp.global.auth.dto.response;

import lombok.Getter;

@Getter
public class LoginResponse {
    private final boolean isDuplicatedLogin;

    public LoginResponse(boolean isDuplicatedLogin) {
        this.isDuplicatedLogin = isDuplicatedLogin;
    }
}
