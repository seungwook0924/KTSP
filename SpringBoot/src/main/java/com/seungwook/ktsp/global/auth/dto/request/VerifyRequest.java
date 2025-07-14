package com.seungwook.ktsp.global.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class VerifyRequest {

    @Schema(description = "이메일 앞자리(@kangwon.ac.kr가 자동으로 붙음)", example = "user123")
    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Size(min = 2, max = 36, message = "이메일은 최소 2자 ~ 최대 36자 입니다.")
    private final String email;

    @Schema(description = "인증코드(숫자+대문자 6자리)", example = "SVUV5Z")
    @NotBlank(message = "인증 코드는 필수 입니다.")
    @Size(min = 6, max = 6, message = "인증코드는 6자입니다.")
    private final String verifyCode;

    public VerifyRequest(String email, String verifyCode) {
        this.email = email;
        this.verifyCode = verifyCode;
    }
}
