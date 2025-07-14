package com.seungwook.ktsp.global.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class VerifyRequest {

    @Schema(description = "이메일 앞자리(@kangwon.ac.kr가 자동으로 붙음)", example = "user123")
    @NotBlank(message = "이메일은 필수 항목입니다.")
    private final String email;

    @Schema(description = "인증코드(숫자+대문자 6자리)", example = "SVUV5Z")
    @NotNull(message = "인증 코드는 필수 입니다.")
    private final String verifyCode;

    public VerifyRequest(String email, String verifyCode) {
        this.email = email;
        this.verifyCode = verifyCode;
    }
}
