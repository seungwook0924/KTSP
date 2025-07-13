package com.seungwook.ktsp.global.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class VerifyRequest {

    @NotBlank(message = "이메일은 필수 항목입니다.")
    private final String email;

    @NotNull(message = "인증 코드는 필수 입니다.")
    private final String verifyCode;

    public VerifyRequest(String email, String verifyCode) {
        this.email = email;
        this.verifyCode = verifyCode;
    }
}
