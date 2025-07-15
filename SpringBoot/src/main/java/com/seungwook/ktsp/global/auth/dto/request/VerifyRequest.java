package com.seungwook.ktsp.global.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class VerifyRequest {

    @Schema(description = "이메일", example = "user123@kangwon.ac.kr")
    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Size(min = 16, max = 40, message = "이메일은 최소 16자 ~ 최대 40자 입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
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
