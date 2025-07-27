package com.seungwook.ktsp.global.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private final boolean isDuplicatedLogin;
}
